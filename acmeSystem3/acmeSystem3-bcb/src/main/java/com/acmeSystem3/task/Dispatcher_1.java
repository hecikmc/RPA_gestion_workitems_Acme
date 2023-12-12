package com.acmeSystem3.task;

import static com.acmeSystem3.utils.Constants.*;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.acmeSystem3.models.WorkItem;
import com.acmeSystem3.models.dao.ManagerDDBB;
import com.acmeSystem3.rpa.AcmePlaneRobotRpa;
import com.acmeSystem3.service.AcmeService;
import com.workfusion.bot.service.SecureEntryDTO;
import com.workfusion.odf2.compiler.BotTask;
import com.workfusion.odf2.core.cdi.Injector;
import com.workfusion.odf2.core.cdi.Requires;
import com.workfusion.odf2.core.task.AdHocTask;
import com.workfusion.odf2.core.task.TaskInput;
import com.workfusion.odf2.core.task.output.TaskRunnerOutput;
import com.workfusion.odf2.core.webharvest.rpa.RpaDriver;
import com.workfusion.odf2.core.webharvest.rpa.RpaFactory;
import com.workfusion.odf2.core.webharvest.rpa.RpaRunner;
import com.workfusion.odf2.service.ControlTowerServicesModule;
import com.workfusion.odf2.service.vault.SecretsVaultService;

@BotTask(requireRpa = false)
@Requires(ControlTowerServicesModule.class)
public class Dispatcher_1 implements AdHocTask {

	private final RpaRunner rpaRunner;
	private SecretsVaultService login;
	private SecretsVaultService login_db;
	private SecureEntryDTO credentials;
	private SecureEntryDTO credentials_db;
	private ManagerDDBB managerDDBB;
	private AcmeService acmeService;
	private List<WorkItem> workItemList;
	private List<WorkItem> workItemListSE;
	
	@Inject
	public Dispatcher_1(Injector injector) {
		RpaFactory rpaFactory = injector.instance(RpaFactory.class);
		this.rpaRunner = rpaFactory.builder(RpaDriver.UNIVERSAL).build();
		this.login = injector.instance(SecretsVaultService.class);
		this.login_db = injector.instance(SecretsVaultService.class);
		this.credentials = login.getEntry(ALIAS_ACME);
		this.credentials_db = login_db.getEntry(ALIAS_BBDD);
		this.managerDDBB = new ManagerDDBB(credentials_db);
		this.acmeService = new AcmeService();
		this.workItemList = new ArrayList<WorkItem>();
		this.workItemListSE = new ArrayList<WorkItem>();
	}
	
    @Override
    public TaskRunnerOutput run(TaskInput taskInput) {
    	rpaRunner.execute(driver -> {
    		AcmePlaneRobotRpa webRobot = new AcmePlaneRobotRpa(driver, credentials);
    		webRobot.login();
    		workItemListSE = managerDDBB.extractSystemErrorCases();
    		if (!workItemListSE.isEmpty()) {
    			acmeService.searchCustomerWork(webRobot, workItemListSE);
    			managerDDBB.updateWorkItemError(workItemListSE);	
    		}
    		workItemList = acmeService.getTotalCasesPending(webRobot, managerDDBB);
    		managerDDBB.insertDatas(workItemList);
    		managerDDBB.desconexionDDBB();
    	});
        return taskInput.asResult()
                .withColumn("example_bot_task_output", "completed_successfully");
    }
}

