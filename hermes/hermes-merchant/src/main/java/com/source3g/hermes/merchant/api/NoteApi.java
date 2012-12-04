package com.source3g.hermes.merchant.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.source3g.hermes.constants.ReturnConstants;
import com.source3g.hermes.entity.note.Note;
import com.source3g.hermes.merchant.service.NoteService;

@Controller
@RequestMapping(value = "note")
public class NoteApi {
	@Autowired
	private NoteService noteService;

	@RequestMapping(value = "add/{merchantId}", method = RequestMethod.POST)
	@ResponseBody
	public String add(@RequestBody Note note, @PathVariable String merchantId) {
		noteService.add(note, merchantId);
		return ReturnConstants.SUCCESS;
	}

}
