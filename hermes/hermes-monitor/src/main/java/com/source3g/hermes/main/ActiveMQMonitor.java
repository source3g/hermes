package com.source3g.hermes.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.management.ObjectName;
import javax.management.QueryExp;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.Broker;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.jmx.BrokerView;
import org.apache.activemq.broker.jmx.BrokerViewMBean;
import org.apache.activemq.broker.jmx.ConnectionViewMBean;
import org.apache.activemq.broker.jmx.ConnectorViewMBean;
import org.apache.activemq.broker.jmx.DestinationViewMBean;
import org.apache.activemq.broker.jmx.DurableSubscriptionViewMBean;
import org.apache.activemq.broker.jmx.JobSchedulerViewMBean;
import org.apache.activemq.broker.jmx.ManagedRegionBroker;
import org.apache.activemq.broker.jmx.ManagementContext;
import org.apache.activemq.broker.jmx.NetworkBridgeViewMBean;
import org.apache.activemq.broker.jmx.NetworkConnectorViewMBean;
import org.apache.activemq.broker.jmx.QueueViewMBean;
import org.apache.activemq.broker.jmx.SubscriptionViewMBean;
import org.apache.activemq.broker.jmx.TopicViewMBean;
import org.apache.activemq.broker.region.Queue;
import org.apache.activemq.broker.region.policy.ConstantPendingMessageLimitStrategy;
import org.apache.activemq.broker.region.policy.PolicyEntry;
import org.apache.activemq.broker.region.policy.PolicyMap;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.transport.TransportListener;
import org.apache.activemq.usage.MemoryUsage;
import org.apache.activemq.usage.StoreUsage;
import org.apache.activemq.usage.SystemUsage;
import org.apache.activemq.usage.TempUsage;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 监控ActiveMQ的各种信息Broker,Connection,Queue,Topic的数量和压栈和出栈
 * 
 * @author longgangbai
 * 
 */
public class ActiveMQMonitor {
	private static final transient Logger LOG = LoggerFactory.getLogger(ActiveMQMonitor.class);

	protected static final int MESSAGE_COUNT = 2000;
	protected BrokerService brokerService;
	protected Connection connection;
	protected String bindAddress = "tcp://localhost:61616";
	// ActiveMQConnectionFactory.DEFAULT_BROKER_BIND_URL;
	protected int topicCount;

	/**
	 * 获取Broker 的AdminView对象
	 * 
	 * @return
	 * @throws Exception
	 */
	public BrokerViewMBean getBrokerAdmin() throws Exception {
		return brokerService.getAdminView();
	}

	/**
	 * 获取所有的QueueViewMBean的
	 * 
	 * @return
	 * @throws Exception
	 */
	public Collection<QueueViewMBean> getQueues() throws Exception {
		BrokerViewMBean broker = getBrokerAdmin();
		if (broker == null) {
			return Collections.EMPTY_LIST;
		}
		ObjectName[] queues = broker.getQueues();
		return getManagedObjects(queues, QueueViewMBean.class);
	}

	/**
	 * 获取所有TopicViewMBean
	 * 
	 * @return
	 * @throws Exception
	 */
	public Collection<TopicViewMBean> getTopics() throws Exception {
		BrokerViewMBean broker = getBrokerAdmin();
		if (broker == null) {
			return Collections.EMPTY_LIST;
		}
		ObjectName[] queues = broker.getTopics();
		return getManagedObjects(queues, TopicViewMBean.class);
	}

	/**
	 * 获取所有DurableSubscriptionViewMBean
	 * 
	 * @return
	 * @throws Exception
	 */
	public Collection<DurableSubscriptionViewMBean> getDurableTopicSubscribers() throws Exception {
		BrokerViewMBean broker = getBrokerAdmin();
		if (broker == null) {
			return Collections.EMPTY_LIST;
		}
		ObjectName[] queues = broker.getDurableTopicSubscribers();
		return getManagedObjects(queues, DurableSubscriptionViewMBean.class);
	}

	/**
	 * 获取所有DurableSubscriptionViewMBean
	 * 
	 * @return
	 * @throws Exception
	 */
	public Collection<DurableSubscriptionViewMBean> getInactiveDurableTopicSubscribers() throws Exception {
		BrokerViewMBean broker = getBrokerAdmin();
		if (broker == null) {
			return Collections.EMPTY_LIST;
		}
		ObjectName[] queues = broker.getInactiveDurableTopicSubscribers();
		return getManagedObjects(queues, DurableSubscriptionViewMBean.class);
	}

	/**
	 * 根据queueName获取queue相关的信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public QueueViewMBean getQueue(String name) throws Exception {
		return (QueueViewMBean) getDestinationByName(getQueues(), name);
	}

	/**
	 * 根据topicName获取Topic相关的信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public TopicViewMBean getTopic(String name) throws Exception {
		return (TopicViewMBean) getDestinationByName(getTopics(), name);
	}

	/**
	 * 获取DestinationViewMBean
	 * 
	 * @return
	 * @throws Exception
	 */
	protected DestinationViewMBean getDestinationByName(Collection<? extends DestinationViewMBean> collection, String name) {
		Iterator<? extends DestinationViewMBean> iter = collection.iterator();
		while (iter.hasNext()) {
			DestinationViewMBean destinationViewMBean = iter.next();
			if (name.equals(destinationViewMBean.getName())) {
				return destinationViewMBean;
			}
		}
		return null;
	}

	/**
	 * 获取所有Mananage
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	protected <T> Collection<T> getManagedObjects(ObjectName[] names, Class<T> type) throws Exception {
		List<T> answer = new ArrayList<T>();
		for (int i = 0; i < names.length; i++) {
			ObjectName name = names[i];
			T value = (T) newProxyInstance(name, type, true);
			if (value != null) {
				answer.add(value);
			}
		}
		return answer;
	}

	/**
	 * 获取所有ConnectionViewMBean
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Collection<ConnectionViewMBean> getConnections() throws Exception {
		String brokerName = getBrokerName();
		ObjectName query = new ObjectName("org.apache.activemq:BrokerName=" + brokerName + ",Type=Connection,*");
		Set<ObjectName> queryResult = queryNames(query, null);
		return getManagedObjects(queryResult.toArray(new ObjectName[queryResult.size()]), ConnectionViewMBean.class);
	}

	/**
	 * 获取所有Connections
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Collection<String> getConnections(String connectorName) throws Exception {
		String brokerName = getBrokerName();
		ObjectName query = new ObjectName("org.apache.activemq:BrokerName=" + brokerName + ",Type=Connection,ConnectorName=" + connectorName + ",*");
		Set<ObjectName> queryResult = queryNames(query, null);
		Collection<String> result = new ArrayList<String>(queryResult.size());
		for (ObjectName on : queryResult) {
			String name = StringUtils.replace(on.getKeyProperty("Connection"), "_", ":");
			result.add(name);
		}
		return result;
	}

	/**
	 * 获取所有ConnectionViewMBean
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public ConnectionViewMBean getConnection(String connectionName) throws Exception {
		connectionName = StringUtils.replace(connectionName, ":", "_");
		String brokerName = getBrokerName();
		ObjectName query = new ObjectName("org.apache.activemq:BrokerName=" + brokerName + ",Type=Connection,*,Connection=" + connectionName);
		Set<ObjectName> queryResult = queryNames(query, null);
		if (queryResult.size() == 0)
			return null;
		ObjectName objectName = queryResult.iterator().next();
		return (ConnectionViewMBean) newProxyInstance(objectName, ConnectionViewMBean.class, true);
	}

	@SuppressWarnings("unchecked")
	public Collection<String> getConnectors() throws Exception {
		String brokerName = getBrokerName();
		ObjectName query = new ObjectName("org.apache.activemq:BrokerName=" + brokerName + ",Type=Connector,*");
		Set<ObjectName> queryResult = queryNames(query, null);
		Collection<String> result = new ArrayList<String>(queryResult.size());
		for (ObjectName on : queryResult)
			result.add(on.getKeyProperty("ConnectorName"));
		return result;
	}

	public ConnectorViewMBean getConnector(String name) throws Exception {
		String brokerName = getBrokerName();
		ObjectName objectName = new ObjectName("org.apache.activemq:BrokerName=" + brokerName + ",Type=Connector,ConnectorName=" + name);
		return (ConnectorViewMBean) newProxyInstance(objectName, ConnectorViewMBean.class, true);
	}

	@SuppressWarnings("unchecked")
	public Collection<NetworkConnectorViewMBean> getNetworkConnectors() throws Exception {
		String brokerName = getBrokerName();

		ObjectName query = new ObjectName("org.apache.activemq:BrokerName=" + brokerName + ",Type=NetworkConnector,*");
		Set<ObjectName> queryResult = queryNames(query, null);
		return getManagedObjects(queryResult.toArray(new ObjectName[queryResult.size()]), NetworkConnectorViewMBean.class);
	}

	public Collection<NetworkBridgeViewMBean> getNetworkBridges() throws Exception {
		String brokerName = getBrokerName();
		ObjectName query = new ObjectName("org.apache.activemq:BrokerName=" + brokerName + ",Type=NetworkBridge,*");
		Set<ObjectName> queryResult = queryNames(query, null);
		return getManagedObjects(queryResult.toArray(new ObjectName[queryResult.size()]), NetworkBridgeViewMBean.class);
	}

	@SuppressWarnings("unchecked")
	public Collection<SubscriptionViewMBean> getQueueConsumers(String queueName) throws Exception {
		String brokerName = getBrokerName();
		queueName = StringUtils.replace(queueName, "\"", "_");
		ObjectName query = new ObjectName("org.apache.activemq:BrokerName=" + brokerName + ",Type=Subscription,destinationType=Queue,destinationName=" + queueName + ",*");
		Set<ObjectName> queryResult = queryNames(query, null);
		return getManagedObjects(queryResult.toArray(new ObjectName[queryResult.size()]), SubscriptionViewMBean.class);
	}

	@SuppressWarnings("unchecked")
	public Collection<SubscriptionViewMBean> getConsumersOnConnection(String connectionName) throws Exception {
		connectionName = StringUtils.replace(connectionName, ":", "_");
		String brokerName = getBrokerName();
		ObjectName query = new ObjectName("org.apache.activemq:BrokerName=" + brokerName + ",Type=Subscription,clientId=" + connectionName + ",*");
		Set<ObjectName> queryResult = queryNames(query, null);
		return getManagedObjects(queryResult.toArray(new ObjectName[queryResult.size()]), SubscriptionViewMBean.class);
	}

	/**
	 * 获取定时执行的队列的信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public JobSchedulerViewMBean getJobScheduler() throws Exception {
		ObjectName name = getBrokerAdmin().getJMSJobScheduler();
		return (JobSchedulerViewMBean) newProxyInstance(name, JobSchedulerViewMBean.class, true);
	}

	public String getBrokerName() throws Exception {
		return brokerService.getBrokerName();
	}

	/**
	 * 获取Broker对象
	 * 
	 * @return
	 * @throws Exception
	 */
	public Broker getBroker() throws Exception {
		return brokerService.getBroker();
	}

	public ManagementContext getManagementContext() {
		return brokerService.getManagementContext();
	}

	public ManagedRegionBroker getManagedBroker() throws Exception {
		BrokerView adminView = brokerService.getAdminView();
		if (adminView == null) {
			return null;
		}
		return adminView.getBroker();
	}

	public void purgeQueue(ActiveMQDestination destination) throws Exception {
		Set destinations = getManagedBroker().getQueueRegion().getDestinations(destination);
		for (Iterator i = destinations.iterator(); i.hasNext();) {
			Destination dest = (Destination) i.next();
			if (dest instanceof Queue) {
				Queue regionQueue = (Queue) dest;
				regionQueue.purge();
			}
		}
	}

	/**
	 * 
	 * @param name
	 * @param query
	 * @return
	 * @throws Exception
	 */
	public Set queryNames(ObjectName name, QueryExp query) throws Exception {
		return getManagementContext().queryNames(name, query);
	}

	/**
	 * 通过JMX获取ActiveMQ各种信息
	 * 
	 * @param objectName
	 * @param interfaceClass
	 * @param notificationBroadcaster
	 * @return
	 */
	public Object newProxyInstance(ObjectName objectName, Class interfaceClass, boolean notificationBroadcaster) {
		return getManagementContext().newProxyInstance(objectName, interfaceClass, notificationBroadcaster);
	}

	/**
	 * 监控内存信息
	 * 
	 * @throws Exception
	 */
	public void monitorMermeryUsage() throws Exception {
		SystemUsage proSystemUsage = brokerService.getProducerSystemUsage();
		printSystemUsage(proSystemUsage);
		SystemUsage syUage = brokerService.getSystemUsage();
		printSystemUsage(syUage);
		SystemUsage consumsyUage = brokerService.getConsumerSystemUsage();
		printSystemUsage(consumsyUage);
	}

	/**
	 * 打印内存信息
	 * 
	 * @param syUage
	 */
	public void printSystemUsage(SystemUsage syUage) {
		String name = syUage.getName();
		System.out.println("SystemUsage name =" + name);
		MemoryUsage memeryUsage = syUage.getMemoryUsage();
		System.out.println("memeryUsage PercentUsage name =" + memeryUsage.getPercentUsage());
		System.out.println("memeryUsage Limit name =" + memeryUsage.getLimit());
		System.out.println("memeryUsage Usage name =" + memeryUsage.getUsage());
		TempUsage tempUsage = syUage.getTempUsage();
		System.out.println("tempUsage PercentUsage name =" + tempUsage.getPercentUsage());
		System.out.println("tempUsage Limit name =" + tempUsage.getLimit());
		System.out.println("tempUsage Usage name =" + tempUsage.getUsage());
		StoreUsage storeUsage = syUage.getStoreUsage();
		System.out.println("storeUsage PercentUsage name =" + storeUsage.getPercentUsage());
		System.out.println("storeUsage Limit name =" + storeUsage.getLimit());
		System.out.println("storeUsage Usage name =" + storeUsage.getUsage());
	}

	/**
	 * 监控消息的方法
	 * 
	 * @throws Exception
	 */
	public void monitorQueueAndTopic() throws Exception {
		System.out.println("==========Connection =================");
		Collection<ConnectionViewMBean> conVBean = getConnections();
		for (ConnectionViewMBean bean : conVBean) {
			System.out.println("remoteAddress:" + bean.getRemoteAddress());
			System.out.println("isActive:" + bean.isActive());
			System.out.println("isConnected:" + bean.isConnected());
		}
		System.out.println("=============Topic =================");
		Collection<TopicViewMBean> topicVBean = getTopics();
		for (TopicViewMBean topicbean : topicVBean) {
			System.out.println("beanName =" + topicbean.getName());
			System.out.println("ConsumerCount =" + topicbean.getConsumerCount());
			System.out.println("DequeueCount =" + topicbean.getDequeueCount());
			System.out.println("EnqueueCount =" + topicbean.getEnqueueCount());
			System.out.println("DispatchCount =" + topicbean.getDispatchCount());
			System.out.println("ExpiredCount =" + topicbean.getExpiredCount());
			System.out.println("MaxEnqueueTime =" + topicbean.getMaxEnqueueTime());
			System.out.println("ProducerCount =" + topicbean.getProducerCount());
			System.out.println("MemoryPercentUsage =" + topicbean.getMemoryPercentUsage());
			System.out.println("MemoryLimit =" + topicbean.getMemoryLimit());
		}
		System.out.println("============Queue===================");
		Collection<QueueViewMBean> queuqVBeanList = getQueues();
		for (QueueViewMBean queuebean : queuqVBeanList) {
			System.out.println(" queue beanName =" + queuebean.getName());
			System.out.println("ConsumerCount =" + queuebean.getConsumerCount());
			System.out.println("DequeueCount =" + queuebean.getDequeueCount());
			System.out.println("EnqueueCount =" + queuebean.getEnqueueCount());
			System.out.println("DispatchCount =" + queuebean.getDispatchCount());
			System.out.println("ExpiredCount =" + queuebean.getExpiredCount());
			System.out.println("MaxEnqueueTime =" + queuebean.getMaxEnqueueTime());
			System.out.println("ProducerCount =" + queuebean.getProducerCount());
			System.out.println("MemoryPercentUsage =" + queuebean.getMemoryPercentUsage());
			System.out.println("MemoryLimit =" + queuebean.getMemoryLimit());
		}
	}

	public void test() throws Exception {
		// 获取初始化信息
		init();
		/*for (int i = 0; i < 10; i++) {
			sendTopic(i);
		}
		for (int i = 0; i < 10; i++) {
			sendPS(i);
		}*/
		monitorQueueAndTopic();
		Thread.sleep(5000);
		/*receiveTopic();
		receivePS();*/
	}

	/**
	 * P2P发送方式
	 * 
	 * @throws JMSException
	 */
	public void sendTopic(int i) throws JMSException {
		Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
		Destination topic = session.createQueue("activemq.queue" + i);
		MessageProducer productor = (MessageProducer) session.createProducer(topic);
		TextMessage txtMessage = session.createTextMessage();
		txtMessage.setText("this is a topic message " + i);
		productor.send(txtMessage);
	}

	/**
	 * Sub/Pub发送方式
	 * 
	 * @throws JMSException
	 */
	public void sendPS(int i) throws JMSException {
		Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
		Destination topic = session.createTopic("activemq.topic" + i);
		MessageProducer productor = (MessageProducer) session.createProducer(topic);
		TextMessage txtMessage = session.createTextMessage();
		txtMessage.setText("this is a topic message " + i);
		productor.send(txtMessage);
	}

	/**
	 * P2P接受方式
	 * 
	 * @throws JMSException
	 */
	public void receiveTopic() throws JMSException {
		Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
		Destination topic = session.createQueue("activemq.queue");

		MessageConsumer consumer = (MessageConsumer) session.createConsumer(topic);
		TextMessage txtMessage = (TextMessage) consumer.receive();
		System.out.println("txtMessage =" + txtMessage.getText());
	}

	/**
	 * Sub/Pub接受方式
	 * 
	 * @throws JMSException
	 */
	public void receivePS() throws JMSException {
		Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
		Destination topic = session.createQueue("activemq.topic");

		MessageConsumer consumer = (MessageConsumer) session.createConsumer(topic);
		TextMessage txtMessage = (TextMessage) consumer.receive();
		System.out.println("txtMessage =" + txtMessage.getText());
	}

	/**
	 * 初始化消息的方法
	 * 
	 * @throws Exception
	 */
	protected void init() throws Exception {
		if (brokerService == null) {
			brokerService = createBroker();
		}
		ActiveMQConnectionFactory factory = createConnectionFactory();
		connection = factory.createConnection();
		// 添加Connection 的状态监控的方法
		monitorConnection(connection);
		// 启动连接
		connection.start();
	}

	/**
	 * 监控台ActiveMQConnection的状态的方法
	 * 
	 * @param connection
	 */
	public void monitorConnection(Connection connection) {
		ActiveMQConnection activemqconnection = (ActiveMQConnection) connection;
		// 添加ActiveMQConnection的监听类
		activemqconnection.addTransportListener(new TransportListener() {

			public void onCommand(Object object) {
				System.out.println("onCommand  object " + object);

			}

			public void onException(IOException ex) {
				System.out.println("onException =" + ex.getMessage());
			}

			public void transportInterupted() {
				System.out.println("transportInterupted =");
			}

			public void transportResumed() {
				System.out.println("transportResumed .........");
			}

		});
	}

	protected void destory() throws Exception {
		connection.close();
		if (brokerService != null) {
			brokerService.stop();
		}
	}

	/**
	 * 创建ActiveMQConnectionFactory
	 * 
	 * @return
	 * @throws Exception
	 */
	protected ActiveMQConnectionFactory createConnectionFactory() throws Exception {
		ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory(bindAddress);
		return cf;
	}

	/***
	 * 创建一个Broker监听进程
	 * 
	 * @return
	 * @throws Exception
	 */
	protected BrokerService createBroker() throws Exception {
		// 创建BrokerService对象
		BrokerService answer = new BrokerService();
		// 配置监听相关的信息
		configureBroker(answer);
		// 启动Broker的启动
		answer.start();

		return answer;
	}

	/**
	 * 配置Broker
	 * 
	 * @param answer
	 * @throws Exception
	 */
	protected void configureBroker(BrokerService answer) throws Exception {
		// 创建持久化信息
		answer.setPersistent(false);
		// 设置采用JMX管理
		answer.setUseJmx(true);
		ConstantPendingMessageLimitStrategy strategy = new ConstantPendingMessageLimitStrategy();
		strategy.setLimit(10);
		PolicyEntry tempQueueEntry = createPolicyEntry(strategy);
		tempQueueEntry.setTempQueue(true);
		PolicyEntry tempTopicEntry = createPolicyEntry(strategy);
		tempTopicEntry.setTempTopic(true);
		PolicyMap pMap = new PolicyMap();
		final List<PolicyEntry> policyEntries = new ArrayList<PolicyEntry>();
		policyEntries.add(tempQueueEntry);
		policyEntries.add(tempTopicEntry);
		pMap.setPolicyEntries(policyEntries);
		answer.setDestinationPolicy(pMap);
		// 绑定url
		answer.addConnector(bindAddress);
		answer.setDeleteAllMessagesOnStartup(true);
	}

	/**
	 * 创建一个配置策略
	 * 
	 * @param strategy
	 * @return
	 */
	private PolicyEntry createPolicyEntry(ConstantPendingMessageLimitStrategy strategy) {
		PolicyEntry policy = new PolicyEntry();
		policy.setAdvisdoryForFastProducers(true);
		policy.setAdvisoryForConsumed(true);
		policy.setAdvisoryForDelivery(true);
		policy.setAdvisoryForDiscardingMessages(true);
		policy.setAdvisoryForSlowConsumers(true);
		policy.setAdvisoryWhenFull(true);
		policy.setProducerFlowControl(false);
		policy.setPendingMessageLimitStrategy(strategy);
		return policy;
	}

	public void object2string(Object object) {
		ToStringBuilder.reflectionToString(object, ToStringStyle.MULTI_LINE_STYLE);
	}

}