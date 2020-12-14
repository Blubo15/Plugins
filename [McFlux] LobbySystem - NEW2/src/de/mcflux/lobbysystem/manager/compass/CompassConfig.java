package de.mcflux.lobbysystem.manager.compass;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import de.mcflux.lobbysystem.LobbySystem;

public class CompassConfig {
	
	private File dir;	
	FileConfiguration compassconfig;
	
	private CompassLogger logger;
	
	public CompassConfig(){
		this.logger = new CompassLogger();
		this.dir = new File(LobbySystem.getLobbySystem().getDataFolder(), "compass_config.yml");
		if(!dir.exists()) {
			try {
				dir.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		this.compassconfig = YamlConfiguration.loadConfiguration(this.dir);
	}
	
	
	public void setProperty(String path, Object value){
		this.compassconfig.set(path, value);
		
		try {
			this.compassconfig.save(this.dir);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Object getPropertyObject(String path){
		return this.compassconfig.get(path);
	}
	
	@SuppressWarnings("deprecation")
	public void setDefault(){
		if(!this.dir.exists()){
			try{
				InputStream var = LobbySystem.getLobbySystem().getResource("compass_config.yml");
				if(var != null){
					YamlConfiguration.loadConfiguration(var).save(this.dir);
				}
			}catch(Exception ex){
				
			}
		}
	}
	
	public CompassLogger getCompassLogger(){
		return this.logger;
	}
	
	public FileConfiguration getCompassConfig(){
		return this.compassconfig;
	}
	
	public File getFile() {
		return this.dir; 
	}
	
	public class CompassLogger extends Logger{
		private String name;
		public CompassLogger(){
			super("LobbySystem/CompassTeleporter",null);
			this.name = "[LobbySystem/CompassTeleporter] ";
			setParent(LobbySystem.getLobbySystem().getLogger());
			setLevel(Level.ALL);
		}
		
		public void log(LogRecord logrecord){
			logrecord.setMessage(this.name+logrecord.getMessage());
			super.log(logrecord);
		}
		
	}

}
