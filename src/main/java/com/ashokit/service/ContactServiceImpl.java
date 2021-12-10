package com.ashokit.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ashokit.entity.Contact;
import com.ashokit.repository.ContactRepository;

@Service
public class ContactServiceImpl implements ContactService {

	@Autowired
	private ContactRepository contactRepo;

	@Override
	public boolean saveContact(Contact contact) {
		contact.setActiveSw(contact.getActiveSw());
		Contact savedObj = contactRepo.save(contact);
		return savedObj.getContactId() != null;
	}

	@Override
	public List<Contact> getAllContacts() {
		return contactRepo.findAll();
	}

	@Override
	public Contact getContactById(Integer contactId) {
		Optional<Contact> findById = contactRepo.findById(contactId);
		if (findById.isPresent()) {
			return findById.get();
		}
		return null;
	}

	@Override
	public boolean updateContact(Contact contact) {
		try {
			contactRepo.save(contact);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean setActiveinActiveRecord(Integer contactId,String flag) {
		try {
			Contact contact = contactRepo.findById(contactId).get();
			contact.setActiveSw(flag);
			contactRepo.save(contact);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
