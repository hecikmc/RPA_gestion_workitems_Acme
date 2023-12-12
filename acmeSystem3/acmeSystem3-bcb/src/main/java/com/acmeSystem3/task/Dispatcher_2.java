package com.acmeSystem3.task;

import static com.acmeSystem3.utils.Constants.ALIAS_ACME;
import static com.acmeSystem3.utils.Constants.ALIAS_BBDD;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.acmeSystem3.models.Customer;
import com.acmeSystem3.models.dao.ManagerDDBB;
import com.acmeSystem3.service.AcmeSystem3Service;
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
import com.workfusion.rpa.helpers.RPA;

@BotTask(requireRpa = false)
@Requires(ControlTowerServicesModule.class)
public class Dispatcher_2 implements AdHocTask {

	private final RpaRunner rpaRunner;
	
	private final SecretsVaultService login;
	private SecureEntryDTO credentials;
	private final SecretsVaultService login_db;
	private SecureEntryDTO credentials_db;
	
	ManagerDDBB managerDDBB;
	AcmeSystem3Service system3Service;
	
	private List<Customer> customerList;
	
	@Inject
	public Dispatcher_2(Injector injector) {
		RpaFactory rpaFactory = injector.instance(RpaFactory.class);
		this.rpaRunner = rpaFactory.builder(RpaDriver.UNIVERSAL).closeOnCompletion(true).build();
		this.login = injector.instance(SecretsVaultService.class);
		this.login_db = injector.instance(SecretsVaultService.class);
		this.credentials = login.getEntry(ALIAS_ACME);
		this.credentials_db = login_db.getEntry(ALIAS_BBDD);
		this.system3Service = new AcmeSystem3Service(credentials);
		this.managerDDBB = new ManagerDDBB(credentials_db);
		this.customerList = new ArrayList<Customer>();
	}
	
    @Override
    public TaskRunnerOutput run(TaskInput taskInput) {
    	rpaRunner.execute(driver -> {
    		system3Service.login();
    		customerList = managerDDBB.extractCustomers();
    		RPA.sleep(8000);
    		customerList = system3Service.searchCustomers(customerList);
    		managerDDBB.updateCustomers(customerList);
    		managerDDBB.desconexionDDBB();
    	});
        return taskInput.asResult()
                .withColumn("example_bot_task_output", "completed_successfully");
    }
}
	