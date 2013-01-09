package com.source3g.hermes.entity.customer;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(value = { "otherPhones", "operateTime","callRecords" })
public interface CustomerForSyncIntf {

}
