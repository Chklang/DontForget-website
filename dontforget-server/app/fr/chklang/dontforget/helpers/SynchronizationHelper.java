package fr.chklang.dontforget.helpers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;
import fr.chklang.dontforget.business.Task;
import fr.chklang.dontforget.business.User;

public class SynchronizationHelper {
	
	public static interface AdapterForUpdate<PK, T extends Model, DTO> {
		Finder<PK, T> getDAO();
		
		String getUuid(T pObject);
		
		String getUuid(DTO pObject);
		
		String getName(T pObject);
		
		String getName(DTO pObject);
		
		User getUser(T pObject);
		
		Set<Task> findByTAndUser(T pObject, User pUser);
		
		void removeTFromTask(Task pTask, T pObject);
		
		void addTToTask(Task pTask, T pObject);
		
		void deleteT(String pUuid, int pIdUser);
		
		T getFromDBByUuid(String pUuid);
		
		T create(String pUuid, User pUser);
		
		T update(T pObject, String pName);
	}
	
	public static <PK, Type extends Model, AAADTO> Map<String, String> updateDTOs(User pConnectedUser, Collection<AAADTO> pDTOs, AdapterForUpdate<PK, Type, AAADTO> pAdapterForUpdate) {
		Map<String, String> lMapUuidCorrections = new HashMap<>();
		
		Map<String, Collection<Type>> lMapTypeDBNames = new HashMap<>();
		Map<String, Type> lMapTypeKeptDBNames = new HashMap<>();
		Map<String, Type> lMapTypeKeptDBUuid = new HashMap<>();
		Map<String, AAADTO> lMapAAADTOsUuids = new HashMap<>();
		
		Map<String, Collection<String>> lMapTypesNamesAfterUpdate = new HashMap<>();
		pDTOs.forEach((pDTO) -> {
			String lUuid = pAdapterForUpdate.getUuid(pDTO);
			lMapAAADTOsUuids.put(lUuid, pDTO);
		});
		
		pAdapterForUpdate.getDAO().all().forEach((pType) -> {
			if (lMapAAADTOsUuids.containsKey(pAdapterForUpdate.getUuid(pType))) {
				String lName = pAdapterForUpdate.getName(pType);

				Collection<Type> lElements = lMapTypeDBNames.get(lName);
				if (lElements == null) {
					lElements = new ArrayList<>();
					lMapTypeDBNames.put(lName, lElements);
				}
				lElements.add(pType);
			}
		});
		//Correction DB
		lMapTypeDBNames.forEach((pName, pTypes) -> {
			if (pTypes.size() <= 1) {
				return;
			}
			//Duplicate names, keep the "first" tag, ordoned by UUID
			Type lTypeToKeep = pTypes.stream().reduce((pType1, pType2) -> {
				int lCompareResult = AbstractStringComparatorByAsciiOrder.compareStatic(pAdapterForUpdate.getUuid(pType1), pAdapterForUpdate.getUuid(pType2));
				if (lCompareResult <= 0) {
					return pType1;
				} else {
					return pType2;
				}
			}).get();
			pTypes.forEach((pType) -> {
				if (pType.equals(lTypeToKeep)) {
					return;
				}
				Set<Task> lTasks = pAdapterForUpdate.findByTAndUser(pType, pConnectedUser);
				lTasks.forEach((lTask) -> {
					pAdapterForUpdate.removeTFromTask(lTask, pType);
					pAdapterForUpdate.addTToTask(lTask, pType);
					lTask.setLastUpdate(System.currentTimeMillis());
					lTask.save();
				});
				pAdapterForUpdate.deleteT(pAdapterForUpdate.getUuid(pType), pConnectedUser.getIdUser());
//				TypeToDelete lTypeToDelete = new TypeToDelete();
//				lTypeToDelete.setDateDeletion(System.currentTimeMillis());
//				lTypeToDelete.setUuidType(pType.getUuid());
//				lTypeToDelete.setIdUser(pConnectedUser.getIdUser());
//				lTypeToDelete.save();
				
				lMapUuidCorrections.put(pAdapterForUpdate.getUuid(pType), pAdapterForUpdate.getUuid(lTypeToKeep));
				pType.delete();
			});
			lMapTypeKeptDBNames.put(pAdapterForUpdate.getName(lTypeToKeep), lTypeToKeep);
			
			Collection<String> lUuidsAfterUpdate = new ArrayList<>();
			lUuidsAfterUpdate.add(pAdapterForUpdate.getUuid(lTypeToKeep));
			lMapTypesNamesAfterUpdate.put(pAdapterForUpdate.getName(lTypeToKeep), lUuidsAfterUpdate);
			lMapTypeKeptDBUuid.put(pAdapterForUpdate.getUuid(lTypeToKeep), lTypeToKeep);
		});
		lMapTypeDBNames.clear();
		lMapAAADTOsUuids.forEach((pUuid, pAAADTO) -> {
			String lNewName = pAdapterForUpdate.getName(pAAADTO);
			
			Type lTypeDB = lMapTypeKeptDBUuid.get(pAdapterForUpdate.getUuid(pAAADTO));
			if (lTypeDB != null) {
				//Must update a name of a tag which is into the DB
				String lOldName = pAdapterForUpdate.getName(lTypeDB);
				Collection<String> lTypesNamesAfterUpdate = lMapTypesNamesAfterUpdate.get(lOldName);
				lTypesNamesAfterUpdate.remove(pAdapterForUpdate.getUuid(lTypeDB));
			}
			Collection<String> lTypesNamesAfterUpdate = lMapTypesNamesAfterUpdate.get(lNewName);
			if (lTypesNamesAfterUpdate == null) {
				lTypesNamesAfterUpdate = new ArrayList<>();
				lMapTypesNamesAfterUpdate.put(lNewName, lTypesNamesAfterUpdate);
			}
			lTypesNamesAfterUpdate.add(pAdapterForUpdate.getUuid(pAAADTO));
		});
		
		//Check if there is some tags with same name
		lMapTypesNamesAfterUpdate.forEach((pName, pUuids) -> {
			if (pUuids.size() <= 1) {
				//one element, ignore treatment
				return;
			}
			//More than one tag with this name, remove duplicates
			String lUuidToKeep = pUuids.stream().reduce((pUuid1, pUuid2) -> {
				int lCompareResult = AbstractStringComparatorByAsciiOrder.compareStatic(pUuid1, pUuid2);
				if (lCompareResult <= 0) {
					return pUuid1;
				} else {
					return pUuid2;
				}
			}).get();
			Type lTypeToKeep = pAdapterForUpdate.getFromDBByUuid(lUuidToKeep);
			pUuids.forEach((pUuidToDelete) -> {
				if (pUuidToDelete.equals(lUuidToKeep)) {
					//Not delete it
					return;
				}
				
				Type lType = pAdapterForUpdate.getFromDBByUuid(pUuidToDelete);
				Set<Task> lTasks = pAdapterForUpdate.findByTAndUser(lType, pConnectedUser);
				lTasks.forEach((lTask) -> {
					pAdapterForUpdate.removeTFromTask(lTask, lType);
					pAdapterForUpdate.addTToTask(lTask, lTypeToKeep);
					lTask.setLastUpdate(System.currentTimeMillis());
					lTask.save();
				});
				pAdapterForUpdate.deleteT(pUuidToDelete, pConnectedUser.getIdUser());
//				TypeToDelete lTypeToDelete = new TypeToDelete();
//				lTypeToDelete.setDateDeletion(System.currentTimeMillis());
//				lTypeToDelete.setUuidType(pUuidToDelete);
//				lTypeToDelete.setIdUser(pConnectedUser.getIdUser());
//				lTypeToDelete.save();

				lMapUuidCorrections.put(pUuidToDelete, lUuidToKeep);
				lType.delete();
			});
		});
		
		//Now update/create tags
		pDTOs.forEach((pAAADTO) -> {
			if (lMapUuidCorrections.containsKey(pAdapterForUpdate.getUuid(pAAADTO))) {
				return;
			}
			Type lTypeDB = pAdapterForUpdate.getFromDBByUuid(pAdapterForUpdate.getUuid(pAAADTO));
			if (lTypeDB == null) {
				//Create it
				lTypeDB = pAdapterForUpdate.create(pAdapterForUpdate.getUuid(pAAADTO), pConnectedUser);
			}
			if (!pConnectedUser.equals(pAdapterForUpdate.getUser(lTypeDB))) {
				//No modifications!
				return;
			}
			pAdapterForUpdate.update(lTypeDB, pAdapterForUpdate.getName(pAAADTO));
			lTypeDB.save();
		});
		
		return lMapUuidCorrections;
	}
}
