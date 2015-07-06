package fr.chklang.dontforget.dto;

import java.util.ArrayList;
import java.util.Arrays;
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
	
	private final Collection<TaskDTO> tasks;
	private final Collection<TagDTO> tags;
	private final Collection<PlaceDTO> places;
	private final Collection<CategoryDTO> categories;
	private final UserDTO user;

	private final Collection<String> uuidTasksToDelete;
	private final Collection<String> uuidTagsToDelete;
	private final Collection<String> uuidPlacesToDelete;
	private final Collection<String> uuidCategoriesToDelete;
	
	public SynchronizationDTO(JsonNode pJson) {
		super(mapper.getNodeFactory());
		tasks = new ArrayList<>();
		pJson.get("tasks").forEach((pJsonNode) -> {
			tasks.add(new TaskDTO(pJsonNode));
		});
		tags = new ArrayList<>();
		pJson.get("tags").forEach((pJsonNode) -> {
			tags.add(new TagDTO(pJsonNode));
		});
		places = new ArrayList<>();
		pJson.get("places").forEach((pJsonNode) -> {
			places.add(new PlaceDTO(pJsonNode));
		});
		categories = new ArrayList<>();
		pJson.get("categories").forEach((pJsonNode) -> {
			categories.add(new CategoryDTO(pJsonNode));
		});
		
		uuidCategoriesToDelete = new ArrayList<>();
		pJson.get("uuidCategoriesToDelete").forEach((pJsonNode) -> {
			uuidCategoriesToDelete.add(pJsonNode.asText());
		});
		uuidTagsToDelete = new ArrayList<>();
		pJson.get("uuidTagsToDelete").forEach((pJsonNode) -> {
			uuidTagsToDelete.add(pJsonNode.asText());
		});
		uuidPlacesToDelete = new ArrayList<>();
		pJson.get("uuidPlacesToDelete").forEach((pJsonNode) -> {
			uuidPlacesToDelete.add(pJsonNode.asText());
		});
		uuidTasksToDelete = new ArrayList<>();
		pJson.get("uuidTasksToDelete").forEach((pJsonNode) -> {
			uuidTasksToDelete.add(pJsonNode.asText());
		});

		if (pJson.has("user")) {
			user = new UserDTO(pJson.get("user"));
		} else {
			user = null;
		}
		
		build();
	}
	
	public SynchronizationDTO(Stream<Task> pTasks, Stream<Tag> pTags, Stream<Place> pPlaces, Stream<Category> pCategories, User pUser, Stream<CategoryToDelete> pUuidCategoriesToDelete, Stream<PlaceToDelete> pUuidPlacesToDelete, Stream<TagToDelete> pUuidTagsToDelete, Stream<TaskToDelete> pUuidTasksToDelete) {
		super(mapper.getNodeFactory());
		
		tasks = Arrays.asList(pTasks.map(new Function<Task, TaskDTO>(){
			@Override
			public TaskDTO apply(Task arg0) {
				return new TaskDTO(arg0);
			}
		}).toArray(TaskDTO[]::new));
		
		tags = Arrays.asList(pTags.map(new Function<Tag, TagDTO>(){
			@Override
			public TagDTO apply(Tag arg0) {
				return new TagDTO(arg0);
			}
		}).toArray(TagDTO[]::new));
		
		places = Arrays.asList(pPlaces.map(new Function<Place, PlaceDTO>(){
			@Override
			public PlaceDTO apply(Place arg0) {
				return new PlaceDTO(arg0);
			}
		}).toArray(PlaceDTO[]::new));
		
		categories = Arrays.asList(pCategories.map(new Function<Category, CategoryDTO>(){
			@Override
			public CategoryDTO apply(Category arg0) {
				return new CategoryDTO(arg0);
			}
		}).toArray(CategoryDTO[]::new));
		
		uuidCategoriesToDelete = Arrays.asList(pUuidCategoriesToDelete.map(new Function<CategoryToDelete, String>(){
			@Override
			public String apply(CategoryToDelete arg0) {
				return arg0.getUuidCategory();
			}
		}).toArray(String[]::new));
		uuidTagsToDelete = Arrays.asList(pUuidTagsToDelete.map(new Function<TagToDelete, String>(){
			@Override
			public String apply(TagToDelete arg0) {
				return arg0.getUuidTag();
			}
		}).toArray(String[]::new));
		uuidPlacesToDelete = Arrays.asList(pUuidPlacesToDelete.map(new Function<PlaceToDelete, String>(){
			@Override
			public String apply(PlaceToDelete arg0) {
				return arg0.getUuidPlace();
			}
		}).toArray(String[]::new));
		uuidTasksToDelete = Arrays.asList(pUuidTasksToDelete.map(new Function<TaskToDelete, String>(){
			@Override
			public String apply(TaskToDelete arg0) {
				return arg0.getUuidTask();
			}
		}).toArray(String[]::new));
		
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
	public Collection<TaskDTO> getTasks() {
		return tasks;
	}

	/**
	 * @return the tags
	 */
	public Collection<TagDTO> getTags() {
		return tags;
	}

	/**
	 * @return the places
	 */
	public Collection<PlaceDTO> getPlaces() {
		return places;
	}

	/**
	 * @return the categories
	 */
	public Collection<CategoryDTO> getCategories() {
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
	public Collection<String> getUuidTasksToDelete() {
		return uuidTasksToDelete;
	}

	/**
	 * @return the uuidTagsToDelete
	 */
	public Collection<String> getUuidTagsToDelete() {
		return uuidTagsToDelete;
	}

	/**
	 * @return the uuidPlacesToDelete
	 */
	public Collection<String> getUuidPlacesToDelete() {
		return uuidPlacesToDelete;
	}

	/**
	 * @return the uuidCategoriesToDelete
	 */
	public Collection<String> getUuidCategoriesToDelete() {
		return uuidCategoriesToDelete;
	}
}
