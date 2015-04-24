package fr.chklang.dontforget.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import fr.chklang.dontforget.ApplicationGlobal;
import fr.chklang.dontforget.business.Category;
import fr.chklang.dontforget.business.Place;
import fr.chklang.dontforget.business.Tag;
import fr.chklang.dontforget.business.Task;
import fr.chklang.dontforget.business.User;

public class SynchronizationDTO extends ObjectNode {
	
	private static final ObjectMapper mapper = new ObjectMapper();
	
	private final Stream<TaskDTO> tasks;
	private final Stream<TagDTO> tags;
	private final Stream<PlaceDTO> places;
	private final Stream<CategoryDTO> categories;
	private final UserDTO user;
	
	public SynchronizationDTO(JsonNode pJson) {
		super(mapper.getNodeFactory());
		Collection<TaskDTO> lTasks = new ArrayList<>();
		pJson.get("tasks").forEach((pJsonNode) -> {
			lTasks.add(new TaskDTO(pJsonNode));
		});
		Collection<TagDTO> lTags = new ArrayList<>();
		pJson.get("tags").forEach((pJsonNode) -> {
			lTags.add(new TagDTO(pJsonNode));
		});
		Collection<PlaceDTO> lPlaces = new ArrayList<>();
		pJson.get("places").forEach((pJsonNode) -> {
			lPlaces.add(new PlaceDTO(pJsonNode));
		});
		Collection<CategoryDTO> lCategories = new ArrayList<>();
		pJson.get("categories").forEach((pJsonNode) -> {
			lCategories.add(new CategoryDTO(pJsonNode));
		});
		
		tasks = lTasks.stream();
		tags = lTags.stream();
		places = lPlaces.stream();
		categories = lCategories.stream();
		
		if (pJson.has("user")) {
			user = new UserDTO(pJson.get("user"));
		} else {
			user = null;
		}
		
		build();
	}
	
	public SynchronizationDTO(Stream<Task> pTasks, Stream<Tag> pTags, Stream<Place> pPlaces, Stream<Category> pCategories, User pUser) {
		super(mapper.getNodeFactory());
		
		tasks = pTasks.map(new Function<Task, TaskDTO>(){
			@Override
			public TaskDTO apply(Task arg0) {
				return new TaskDTO(arg0);
			}
		});
		
		tags = pTags.map(new Function<Tag, TagDTO>(){
			@Override
			public TagDTO apply(Tag arg0) {
				return new TagDTO(arg0);
			}
		});
		
		places = pPlaces.map(new Function<Place, PlaceDTO>(){
			@Override
			public PlaceDTO apply(Place arg0) {
				return new PlaceDTO(arg0);
			}
		});
		
		categories = pCategories.map(new Function<Category, CategoryDTO>(){
			@Override
			public CategoryDTO apply(Category arg0) {
				return new CategoryDTO(arg0);
			}
		});
		
		if (pUser == null) {
			user = null;
		} else {
			user = new UserDTO(pUser);
		}
		build();
	}
	
	private void build() {
		ArrayNode lTasks = this.arrayNode();
		tasks.forEach((pTask) -> {
			lTasks.add(pTask);
		});
		this.put("tasks", lTasks);
		
		ArrayNode lTags = this.arrayNode();
		tags.forEach((pTag) -> {
			lTags.add(pTag);
		});
		this.put("tags", lTags);
		
		ArrayNode lPlaces = this.arrayNode();
		places.forEach((pPlace) -> {
			lPlaces.add(pPlace);
		});
		this.put("places", lPlaces);
		
		ArrayNode lCategories = this.arrayNode();
		categories.forEach((pCategory) -> {
			lCategories.add(pCategory);
		});
		this.put("categories", lCategories);
		
		if (user != null) {
			this.put("user", user);
		}
		
		this.put("version", ApplicationGlobal.VERSION);
	}

	/**
	 * @return the tasks
	 */
	public Stream<TaskDTO> getTasks() {
		return tasks;
	}

	/**
	 * @return the tags
	 */
	public Stream<TagDTO> getTags() {
		return tags;
	}

	/**
	 * @return the places
	 */
	public Stream<PlaceDTO> getPlaces() {
		return places;
	}

	/**
	 * @return the categories
	 */
	public Stream<CategoryDTO> getCategories() {
		return categories;
	}

	/**
	 * @return the user
	 */
	public UserDTO getUser() {
		return user;
	}
}
