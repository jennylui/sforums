package sforums.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import sforums.domain.Category;

@XmlRootElement(name = "categories")
public class CategoryList extends IdentifiableEntityList<Category> {

	public CategoryList() {
	}

	public CategoryList(List<Category> categories) {
		super(categories);
	}

	@XmlElement(name = "category")
	public List<Category> getCategories() {
		return super.getList();
	}
}
