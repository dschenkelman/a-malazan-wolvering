package module;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import org.robolectric.Robolectric;

import phoneticket.android.appliaction.PhoneTicketModule;

import roboguice.RoboGuice;
import roboguice.config.DefaultRoboModule;
import roboguice.inject.RoboInjector;

import android.app.Application;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.util.Modules;

public class TestModule extends AbstractModule {
	 
	  private HashMap<Class<?>, Object> bindings;
	 
	  public TestModule() {
	    bindings = new HashMap<Class<?>, Object>();
	  }
	 
	  @Override
	  @SuppressWarnings("unchecked")
	    protected void configure() {
	    Set<Entry<Class<?>, Object>> entries = bindings.entrySet();
	    for (Entry<Class<?>, Object> entry : entries) {
	      bind((Class<Object>) entry.getKey()).toInstance(entry.getValue());
	    }
	  }
	 
	  public void addBinding(Class<?> type, Object object) {
	    bindings.put(type, object);
	  }
	 
	  public static void setUp(Object testObject, TestModule module) {
	    Module roboGuiceModule = RoboGuice.newDefaultRoboModule(Robolectric.application);
	    Module productionModule = Modules.override(roboGuiceModule ).with(new PhoneTicketModule());
	    Module testModule = Modules.override(productionModule).with(module);
	    RoboGuice.setBaseApplicationInjector(Robolectric.application, RoboGuice.DEFAULT_STAGE, testModule);
	    RoboInjector injector = RoboGuice.getInjector(Robolectric.application);
	    injector.injectMembers(testObject);
	  }
	 
	  public static void tearDown() {
	    RoboGuice.util.reset();
	    Application app = Robolectric.application;
	    DefaultRoboModule defaultModule = RoboGuice.newDefaultRoboModule(app);
	    RoboGuice.setBaseApplicationInjector(app, RoboGuice.DEFAULT_STAGE, defaultModule);
	  }
	 
	}