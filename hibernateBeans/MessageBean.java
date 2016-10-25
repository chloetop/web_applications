/**
 * 
 */
package edu.unsw.comp9321.hibernateBeans;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author Millyto
 *
 */
public class MessageBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private long messageId;
	private String subject;
	private String content;
	private UserBean user;
	private int status;
	
	public MessageBean() {
	}
	
	public MessageBean(String subject, String content, UserBean user) {
	  super();
	  this.subject = subject;
	  this.content = content;
	  this.user = user;
  }
	
	private Timestamp insertTime;
	
	public long getMessageId() {
		return messageId;
	}
	public void setMessageId(long messageId) {
		this.messageId = messageId;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public UserBean getUser() {
		return user;
	}
	public void setUser(UserBean user) {
		this.user = user;
	}
	public Timestamp getInsertTime() {
		return insertTime;
	}
	public void setInsertTime(Timestamp insertTime) {
		this.insertTime = insertTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
