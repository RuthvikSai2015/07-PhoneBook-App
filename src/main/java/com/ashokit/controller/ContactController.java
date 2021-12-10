package com.ashokit.controller;

import java.util.List;
import java.util.Map;

import javax.websocket.server.PathParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ashokit.constants.AppConstants;
import com.ashokit.entity.Contact;
import com.ashokit.props.AppProperties;
import com.ashokit.service.ContactService;

@RestController
@CrossOrigin
public class ContactController {

	private static final Logger logger = LoggerFactory.getLogger(ContactController.class);

	@Autowired
	private ContactService contactService;

	@Autowired
	private AppProperties appProps;

	@GetMapping("/loadForm")
	public String loadForm(Model model) {
		logger.debug("********loadForm() execution started*********");
		model.addAttribute("contact", new Contact());
		logger.debug("******loadForm() execution completed********");
		logger.info("*******loadForm() executed successfully....*******");
		return AppConstants.INDEX_VIEW;
	}

	@PostMapping("/saveContact")
	public boolean handleSaveContactBtn(@RequestBody Contact contact) {
		logger.debug("******saveContact execution started*********");
		Map<String, String> messages = appProps.getMessages();
		String msgTxt = null;
		if (contact.getContactId() == null) {
			msgTxt = messages.get(AppConstants.SAVE_SUCCESS);
			contact.setActiveSw("Y");
		} else {
			contact.setActiveSw(contact.getActiveSw());
			msgTxt = messages.get(AppConstants.UPDATE_SUCCESS);
		}
		boolean isSaved = contactService.saveContact(contact);
		if (isSaved) {
			logger.info("**** Contact Saved Successfully *****");
			//model.addAttribute(AppConstants.SUCCESS_MSG, msgTxt);
		} else {
			logger.info("**** Contact Save Failed *****");
			//model.addAttribute(AppConstants.ERROR_MSG, messages.get(AppConstants.SAVE_FAIL));
		}
		logger.debug("******saveContact execution ended*********");
		return isSaved;
	}

	@GetMapping("/viewContacts")
	public ResponseEntity<?> handleViewContactsHyperlink(Model model) {
		logger.debug("****** viewContacts execution started *****");
		List<Contact> contactsList = contactService.getAllContacts();
		if (contactsList.isEmpty()) {
			logger.info("***** Contacts Are Not Available in DB *****");
		}
		model.addAttribute("contacts", contactsList);
		logger.debug("****** viewContacts execution ended *****");
		logger.info("****** viewContacts execution completed successfully *****");
		return new ResponseEntity<List>(contactsList,HttpStatus.OK);

	}

	@GetMapping("/getcontact/{contactId}")
	public Contact handleEditClick(@PathVariable("contactId") Integer cid) {
		logger.debug("****** execution started for edit click ******");
		Contact contactObj = contactService.getContactById(cid);
	//	model.addAttribute("contact", contactObj);
		logger.debug("****** execution completed for edit click ******");
		logger.debug("****** execution completed successfully for edit click ******");
		return contactObj;
	}

	@PutMapping("/update/{id}/{flag}")
	public boolean handleDeleteClick(@PathVariable("id") Integer cid,@PathVariable("flag") String flag) {
		logger.debug("**** execution started for deleting contact ****");
		boolean result = contactService.setActiveinActiveRecord(cid, flag);
		logger.debug("**** execution completed for deleting contact ****");
		logger.info("**** Contact Deleted Successfully ****");
		return result;
	}
}
