package edu.unsw.comp9321.hibernateDao;

import java.util.List;

import edu.unsw.comp9321.hibernateBeans.MessageBean;

public interface MessageDAO {
	public List<MessageBean> getMessagesByUsername(String username);
	public List<MessageBean> getMessagesNotSent();
	public void addMessage(MessageBean message); 
	public void updateMessage(long id, int status);
}
