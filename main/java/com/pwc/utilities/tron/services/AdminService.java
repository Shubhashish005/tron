package com.pwc.utilities.tron.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.pwc.utilities.tron.model.entity.Dashboard;
import com.pwc.utilities.tron.model.entity.DbObj;
import com.pwc.utilities.tron.model.entity.Environment;
import com.pwc.utilities.tron.model.entity.GeneralSetting;
import com.pwc.utilities.tron.model.entity.InstallRecord;
import com.pwc.utilities.tron.model.entity.LocalUser;
import com.pwc.utilities.tron.model.entity.Notification;
import com.pwc.utilities.tron.model.entity.Patch;
import com.pwc.utilities.tron.model.entity.PatchApp;
import com.pwc.utilities.tron.model.entity.PatchDb;
import com.pwc.utilities.tron.model.entity.Permission;
import com.pwc.utilities.tron.model.entity.Prerequisite;
import com.pwc.utilities.tron.model.entity.Role;
import com.pwc.utilities.tron.model.entity.ServicePack;


public interface AdminService {
	
	Iterable<Environment> getAllEnvironemnts();
	Environment getEnvironment(Integer envID);
	Environment addEnvironment(Environment environment) throws Exception;
	void deleteEnvironment(Environment environment);
	void deleteEnvironment(Integer envId);
	List<Map<String, String>> getAppliedPackageList(Environment environment, String path);
	Map<String,String> readFile(String path) throws IOException;
	List<Map<String, String>> compareEnvs(Environment source, Environment target);
	
		
	Iterable<Patch> getPatches();
	Patch getPatch(Integer patchId);
	Patch addPatch(Patch patch);
	void deletePatch(Integer patchId);
	
	Iterable<DbObj> getAllDBObject();
	DbObj getDBObject(Integer dbObjId);
	DbObj addDBObject(DbObj dbObj);
	void deleteDBBObject(Integer dbObjId);
	void deleteDBBObject(DbObj dbObject);
	
	Iterable<PatchDb> getAllPatchDB(); 
	PatchDb getPatchDB(Integer patchId);
	PatchDb addPatchDb(PatchDb patchId);
	void deletePatchDB(Integer patchId);
	void deletePatchDB(PatchDb patchDB);
	
	Iterable<PatchApp> getAllPatchApp(Patch patch);
	PatchApp getPatchApp(Integer patchId);
	PatchApp addPatchApp(PatchApp patchId);
	void deletePatchApp(Integer patchId);
	void deletePatchApp(PatchApp patchDB);
	
	Iterable<ServicePack> getAllServicePackes();
	ServicePack getServicePack(Integer spId);
	ServicePack addServicePack(ServicePack servicePack);
	void deleteServicePack(Integer spId);
	void deleteServicePack(ServicePack servicePack);
	
	Iterable<InstallRecord> getAllInstallRecords();
	InstallRecord getInstallRecord(Integer installId);
	InstallRecord addInstallRecord(InstallRecord installRecord);
	void deleteInstallRecord(Integer installId);
	void deleteInstallRecord(InstallRecord installRecord);
	
	Iterable<Dashboard> getDashboard();
	
	Iterable<Object> getContentOfPatch();
	
	public void createNewEnvironment(String dbHost, int dbPort, String dbPswd, String dbUser, String dbSid,	String envName, String envPath, String envVer, String project);
	
	Iterable<Prerequisite> getAllPrereqs();
	Iterable<Prerequisite> getAllPrereqs(Integer patchId);
	Prerequisite addPrerequisite(Prerequisite prerequisite);
	void deletePrerequisites(Integer patchId);
	
	void applyPrereq(String packageName, Environment environment) throws IOException, InterruptedException;
	void applyPackage(String packageName, Environment environment) throws IOException;
	void applyBlueprint(String packageName, Environment environment) throws IOException, InterruptedException;
	void applyBundle(String packageName, Environment environment) throws  IOException, InterruptedException;
	void unzip(String packageName);
	
	Iterable<Notification> getAllNotifications();
	void deleteAllNotifications();
	Notification createNotification(String message, String status);
	Notification updateNotification(Notification notification);
	
	Map<String, List<HashMap<String, String>>> retreiveSysTableLst(String app);
	void createCodePackage(List<Map<String, String>> fileDetails, File envPath, File file, Patch patch);
	
	void zipFolder(String srcFolder, String destZipFile) throws Exception;
	void applySP(String spName, Environment environment) throws IOException, InterruptedException;
	
	Iterable<GeneralSetting> getSettings();
	GeneralSetting updateSetting(GeneralSetting gs); 
	
	
	// LocalUser
	LocalUser addLocalUser(LocalUser user);
	LocalUser getLocalUser(Integer id);
	LocalUser getLocalUserByGuid(String guid);
	Iterable<LocalUser> getAllLocalUser();
	void deleteLocalUser(LocalUser user);
	
	// Roles
	Iterable<Role> getRoles();
	Role createRole(Role role);
	
	// Permission
	Iterable<Permission> getPermissions();

	

}
