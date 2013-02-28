package sforums.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import sforums.domain.Reply;

@XmlRootElement(name = "replies")
public class ReplyList extends IdentifiableEntityList<Reply> {
	public ReplyList() {
	}

	public ReplyList(List<Reply> replies) {
		super(replies);
	}

	@Override
	@XmlElement(name = "reply")
	public List<Reply> getList() {
		return super.getList();
	}
}
