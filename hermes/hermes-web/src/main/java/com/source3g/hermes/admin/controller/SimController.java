package com.source3g.hermes.admin.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.source3g.hermes.entity.sim.SimInfo;
import com.source3g.hermes.utils.ConfigParams;
import com.source3g.hermes.utils.Page;

@Controller
@RequestMapping("/admin/sim")
@RequiresRoles("admin")
public class SimController {
	// private static final Logger logger =
	// LoggerFactory.getLogger(MerchantController.class);
	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping(value = "import", method = RequestMethod.GET)
	public ModelAndView toImport() {
		return new ModelAndView("admin/sim/import");
	}

	// 验证SIM卡号是否存在
	@RequestMapping(value = "simValidate", method = RequestMethod.GET)
	@ResponseBody
	public Boolean accountValidate(String no) {
		String uri = ConfigParams.getBaseUrl() + "sim/simValidate/" + no + "/";
		Boolean result = restTemplate.getForObject(uri, Boolean.class);
		return result;
	}

	@RequestMapping(value = "/import", method = RequestMethod.POST)
	public String importSim(MultipartFile Filedata) throws IOException {
		String uri = ConfigParams.getBaseUrl() + "sim/import";
		File fileToCopy = new File("/temp/file/" + new Date().getTime() + Filedata.getOriginalFilename().substring(Filedata.getOriginalFilename().lastIndexOf("."), Filedata.getOriginalFilename().length()));
		FileUtils.copyInputStreamToFile(Filedata.getInputStream(), fileToCopy);
		Resource resource = new FileSystemResource(fileToCopy);
		MultiValueMap<String, Object> formData = new LinkedMultiValueMap<String, Object>();
		formData.add("file", resource);
		formData.add("oldName", Filedata.getOriginalFilename());
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(formData, requestHeaders);
		restTemplate.postForObject(uri, requestEntity, String.class);
		return "redirect:admin/sim/list";
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(SimInfo sim, String pageNo, Model model) {
		if (StringUtils.isEmpty(pageNo)) {
			pageNo = "1";
		}
		String uri = ConfigParams.getBaseUrl() + "sim/list/?pageNo=" + pageNo;
		if (StringUtils.isNotEmpty(sim.getServiceNo())) {
			uri += "&serviceNo=" + sim.getServiceNo();
		}
		if (StringUtils.isNotEmpty(sim.getImsiNo())) {
			uri += "&imsiNo=" + sim.getImsiNo();
		}
		model.addAttribute("sim", sim);
		Page page = restTemplate.getForObject(uri, Page.class);
		model.addAttribute("page", page);
		return "/admin/sim/list";
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public ModelAndView deleteById(@PathVariable String id) {
		String uri = ConfigParams.getBaseUrl() + "sim/delete/" + id + "/";
		restTemplate.getForObject(uri, String.class);
		return new ModelAndView("redirect:/admin/sim/list/");
	}

}
