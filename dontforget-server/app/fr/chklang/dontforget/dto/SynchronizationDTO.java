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
import fr.chklang.dontforget.business.CategoryToDelete;
import fr.chklang.dontforget.business.Place;
import fr.chklang.dontforget.business.PlaceToDelete;
import fr.chklang.dontforget.business.Tag;
import fr.chklang.dontforget.business.TagToDelete;
import fr.chklang.dontforget.business.Task;
import fr.chklang.dontforget.business.TaskToDelete;
import fr.chklang.dontforget.business.User;

public class SynchronizationDTO extends ObjectNode {
	
	private static final ObjectMapper mapper = new ObjectMapper();
	
	private final Stream<TaskDTO> tasks;
	private final Stream<TagDTO> tags;
	private final Stream<PlaceDTO> places;
	private final Stream<CategoryDTO> categories;
	private final UserDTO user;

	private final Stream<String> uuidTasksToDelete;
	private final Stream<String> uuidTagsToDelete;
	private final Stream<String> uuidPlacesToDelete;
	private final Stream<String> uuidCategoriesToDelete;
	
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
		
		Collection<String> lUuidCategoriesToDelete = new ArrayList<>();
		pJson.get("uuidCategoriesToDelete").forEach((pJsonNode) -> {
			lUuidCategoriesToDelete.add(pJsonNode.asText());
		});
		Collection<String> lUuidTagsToDelete = new ArrayList<>();
		pJson.get("uuidTagsToDelete").forEach((pJsonNode) -> {
			lUuidTagsToDelete.add(pJsonNode.asText());
		});
		Collection<String> lUuidPlacesToDelete = new ArrayList<>();
		pJson.get("uuidPlacesToDelete").forEach((pJsonNode) -> {
			lUuidPlacesToDelete.add(pJsonNode.asText());
		});
		Collection<String> lUuidTasksToDelete = new ArrayList<>();
		pJson.get("uuidTasksToDelete").forEach((pJsonNode) -> {
			lUuidTasksToDelete.add(pJsonNode.asText());
		});
		
		tasks = lTasks.stream();
		tags = lTags.stream();
		places = lPlaces.stream();
		categories = lCategories.stream();
		
		uuidTasksToDelete = lUuidTasksToDelete.stream();
		uuidTagsToDelete = lUuidTagsToDelete.stream();
		uuidPlacesToDelete = lUuidPlacesToDelete.stream();
		uuidCategoriesToDelete = lUuidCategoriesToDelete.stream();
		
		if (pJson.has("user")) {
			user = new UserDTO(pJson.get("user"));
		} else {
			user = null;
		}
		
		build();
	}
	
	public SynchronizationDTO(Stream<Task> pTasks, Stream<Tag> pTags, Stream<Place> pPlaces, Stream<Category> pCategories, User pUser, Stream<CategoryToDelete> pUuidCategoriesToDelete, Stream<PlaceToDelete> pUuidPlacesToDelete, Stream<TagToDelete> pUuidTagsToDelete, Stream<TaskToDelete> pUuidTasksToDelete) {
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
		
		uuidCategoriesToDelete = pUuidCategoriesToDelete.map(new Function<CategoryToDelete, String>(){
			@Override
			public String apply(CategoryToDelete arg0) {
				return arg0.getUuidCategory();
			}
		});
		uuidTagsToDelete = pUuidTagsToDelete.map(new Function<TagToDelete, String>(){
			@Override
			public String apply(TagToDelete arg0) {
				return arg0.getUuidTag();
			}
		});
		uuidPlacesToDelete = pUuidPlacesToDelete.map(new Function<PlaceToDelete, String>(){
			@Override
			public String apply(PlaceToDelete arg0) {
				return arg0.getUuidPlace();
			}
		});
		uuidTasksToDelete = pUuidTasksToDelete.map(new Function<TaskToDelete, String>(){
			@Override
			public String apply(TaskToDelete arg0) {
				return arg0.getUuidTask();
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
		
		ArrayNode lUuidCategoriesToDelete = this.arrayNode();
		uuidCategoriesToDelete.forEach((pCategory) -> {
			lUuidCategoriesToDelete.add(pCategory);
		});
		this.put("uuidCategoriesToDelete", lUuidCategoriesToDelete);
		
		ArrayNode lUuidTagsToDelete = this.arrayNode();
		uuidTagsToDelete.forEach((pCategory) -> {
			lUuidTagsToDelete.add(pCategory);
		});
		this.put("uuidTagsToDelete", lUuidTagsToDelete);
		
		ArrayNode lUuidPlacesToDelete = this.arrayNode();
		uuidPlacesToDelete.forEach((pCategory) -> {
			lUuidPlacesToDelete.add(pCategory);
		});
		this.put("uuidPlacesToDelete", lUuidPlacesToDelete);
		
		ArrayNode lUuidTasksToDelete = this.arrayNode();
		uuidTasksToDelete.forEach((pCategory) -> {
			lUuidTasksToDelete.add(pCategory);
		});
		this.put("uuidTasksToDelete", lUuidTasksToDelete);
		
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

	/**
	 * @return the uuidTasksToDelete
	 */
	public Stream<String> getUuidTasksToDelete() {
		return uuidTasksToDelete;
	}

	/**
	 * @return the uuidTagsToDelete
	 */
	public Stream<String> getUuidTagsToDelete() {
		return uuidTagsToDelete;
	}

	/**
	 * @return the uuidPlacesToDelete
	 */
	public Stream<String> getUuidPlacesToDelete() {
		return uuidPlacesToDelete;
	}

	/**
	 * @return the uuidCategoriesToDelete
	 */
	public Stream<String> getUuidCategoriesToDelete() {
		return uuidCategoriesToDelete;
	}
}
