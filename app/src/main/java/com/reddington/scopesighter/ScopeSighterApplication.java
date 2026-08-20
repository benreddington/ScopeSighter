package com.reddington.scopesighter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import android.app.Application;
import android.os.Environment;

/**
 * Manages all scopes and ranges
 * Manages writing and reading to external storage
 * Collects all session data and calculates the result for display
 * @author Benjamin Reddington 2013
 */
public class ScopeSighterApplication extends Application{
	
	Target t;                       //set by SightingSession for operations. Contains the list of hits.

	ArrayList<Savable> savables = new ArrayList<Savable>();
		
	String horizontalRotation;
	String verticalRotation;
		
	float deviceHeight;
	float deviceWidth;	
	
	Result r;

	@Override
	/**
	 * Loads saved targets and ranges, and sets the 
	 * units of the app to the units stored in the instance 
	 * of UnitsStatus in savables. If this is a fresh update 
	 * it creates a UnitStatus set to imperial. This allows
	 * ScopeSighterHome to initialize to the units that match
	 * the loaded savables.
	 */	
	public void onCreate(){		
		t = new Target("Target One");
		loadSavables();

		//ONLY NEED TO DO SOMETHING HERE IF NO UNITSTATUS OBJECT EXISTS!!!
		if(savables.size() != 0){			               //if there was a save file to load from at all...
			boolean hasUnitStatus = false;
			for(Savable s:savables){                       //check for a UnitStatus				
				if (s instanceof UnitStatus){              //if the save file already has a UnitStatus, we're good. No action needed
					hasUnitStatus = true;
				}
			}
			if(!hasUnitStatus){ //if the save file exists AND doesn't have a UnitStatus...create one as imperial
				savables.add(new UnitStatus(true));
			}
		}
		else{//if savables.size() == 0, there was no save file and there is no UnitStatus object
			savables.add(new UnitStatus(true)); //add new UnitStatus object set to imperial
		}
	}
	
	public void setDeviceHeight(float h){
		deviceHeight = h;		
	}
	
	public float getDeviceHeight(){
		return deviceHeight;		
	}
	
	public void setDeviceWidth(float w){
		deviceWidth = w;
	}
	
	public float getDeviceWidth(){
		return deviceWidth;		
	}	
	
	public void setTarget(Target t){ 
		this.t = t;
	}
	
	public Target getTarget(){
		return t;
	}
	
	//how to get around that final return call? Never used.
	public Scope getActiveScope(){
		for(Savable s:savables){
			if(s instanceof Scope){
				if(s.getActiveStatus())
					return (Scope)s;
			}
		}
		Scope s = new Scope();
		return s;
	}	
	
	public void setActiveScope(String scopeName){
		for(Savable s: savables){
			if(s instanceof Scope){
				if(s.getName().equals(scopeName))     //is this the scope with the specified name?
					s.setActiveStatus(true);          //set to active
				else
					s.setActiveStatus(false);         //otherwise, set not active 
			}
		}
	}
	
	//***How can the final return be removed? It'll never be reached.
	public Range getActiveRange(){		
		for(Savable s:savables){
			if(s instanceof Range){			//is it a range?
				if (s.getActiveStatus()){   //is it active?
					return (Range)s;        //return it
				}
			}
		}
		Range activeRange = new Range();
		return activeRange;
	}	
	
	public void setActiveRange(String rangeName){
		//reset all ranges to active = false except the specified;
		for(Savable s: savables){
			if(s instanceof Range){
				if(s.getName().equals(rangeName))
					s.setActiveStatus(true);
				else
					s.setActiveStatus(false);
			}
		}		
	}
	
	public ArrayList<Range> getRanges(){		
		//updateSavables guarantees >0 Ranges in savables
		ArrayList<Range> rangeList = new ArrayList<Range>();		
		//add all the ranges in savables to rangeList
		for (Savable sa : savables){
			if (sa instanceof Range){
				rangeList.add((Range)sa);
			}
		}
		return rangeList;
	}
	
	public ArrayList<Scope> getScopes(){		
		//updateSavables guarantees >0 Ranges in savables
		ArrayList<Scope> scopeList = new ArrayList<Scope>();		
		//add all the scopes in savables to scopeList
		for (Savable sa : savables){
			if (sa instanceof Scope){
				scopeList.add((Scope)sa);
			}
		}				
		return scopeList;		
	}
	/**
	 * Returns true for imperial
	 * Returns false for metric
	 * @return
	 */
	public boolean isAppImperial(){
		boolean isImperial = true;
		for(Savable s:savables){
			if (s instanceof UnitStatus){
				isImperial = ((UnitStatus)s).getIsImperial();
			}
		}
		return isImperial;
	}
	/**
	 * This is where the meaningful changes are called to convert the
	 * savables to one or the other unit type.
	 */
	public void setUnitsImperial(){		
		convertSavablesToImperial(); //UnitsStatus will be changed in this convert... method call
	}
	
	public void setUnitsMetric(){
		convertSavablesToMetric(); //UnitStatus will be changed in this convert... method call
	}
/**
 * Creates a result object from the target, scope, and range data.
 */
	public void calculate(){
	
		Scope activeScope = getActiveScope();
		Range activeRange = getActiveRange();
		
		ArrayList<Hit> hits = t.getHits();
		float radius = t.getPixelDiameter()/2;
		//determine the aggregate deviation of the hits for both axes 
		float totalDistanceFromOriginX = 0;
		float totalDistanceFromOriginY = 0;
		
		for (Hit h : hits) {			
			totalDistanceFromOriginX += h.getX();
			totalDistanceFromOriginY += h.getY();
		}
		
		float avgDistanceFromOriginX = totalDistanceFromOriginX / hits.size(); //calculates average deviation in pixels/whatevers X axis
		float avgDistanceFromOriginY = totalDistanceFromOriginY / hits.size(); //calculates average deviation in pixels/whatevers Y axis
				
		//determine vertical click direction
		if((avgDistanceFromOriginY < radius) && (activeScope.getClockwiseOffsetsUp())){
			verticalRotation = "counter-clockwise";				
		}
		else if((avgDistanceFromOriginY > radius) && (activeScope.getClockwiseOffsetsUp())){
			verticalRotation = "clockwise";
		}
		else if((avgDistanceFromOriginY > radius) && (!activeScope.getClockwiseOffsetsUp())){
			verticalRotation = "counter-clockwise";
		}
		else{ //if((totalDistanceFromOriginY < radius) && (!s.getClockwiseIsUp()))
			verticalRotation = "clockwise";
		}		
		
		//determine horizontal click direction
		if((avgDistanceFromOriginX > radius) && (activeScope.getClockwiseOffsetsLeft())){
			horizontalRotation = "clockwise";
		}
		else if((avgDistanceFromOriginX < radius) && (activeScope.getClockwiseOffsetsLeft())){
			horizontalRotation = "counter-clockwise";
		}
		else if((avgDistanceFromOriginX < radius) && (!activeScope.getClockwiseOffsetsLeft())){
			horizontalRotation = "clockwise";
		}
		else{ //if((totalDistanceFromOriginX > radius) && (!s.getClockwiseIsLeft()))
			horizontalRotation = "counter-clockwise";
		}
		
		//We have the direction (shots placing high or low from the above logic,
		//now we need to figure the # of clicks per axis. This just determines
		//the magnitude of the deviation from the middle of each axis. What percent
		//of pixels need to be adjusted for to make the average distances zero?
		float percentDevX = Math.abs((avgDistanceFromOriginX - radius)/t.getPixelDiameter()); //Gets the absolute X distance from center expressed as percent of target diameter 
		float percentDevY = Math.abs((avgDistanceFromOriginY - radius)/t.getPixelDiameter()); //Gets the absolute Y distance from center expressed as percent of target diameter
		
		int clicksX = 0;
		int clicksY = 0;
		
		//calculate the clicks for each axis using metric math
		//getUnits returns false for metric, true for imperial
		if(!isAppImperial()){
		 	float cmDevX = percentDevX * activeRange.getTargetDiameter();   //convert percentDev to cmDev
		 	float cmDevY = percentDevY * activeRange.getTargetDiameter();	//convert percentDev to cmDev
		  
		  	double clickEffectDistance = activeScope.getOffsetPerClick()*(activeRange.getDistanceToTarget()/activeScope.getDistanceForAdjust());
		  	//           CED           =                   cm per click *     actual meters to target / scope meters to target		 
		 	//translate the centimeters into clicks
	 		clicksX = (int)(cmDevX/clickEffectDistance); //cm deviation/CED
	 		clicksY = (int)(cmDevY/clickEffectDistance); //cm deviation/CED
		 }
		 //calculate the clicks for each axis using imperial math
		 else{		 
			 //translate the % into inches
			 float inchesDevX = percentDevX * activeRange.getTargetDiameter();
			 float inchesDevY = percentDevY * activeRange.getTargetDiameter();

			 //calculate effect of one click at the the activeRange distance to target
			 double clickEffectDistance = activeScope.getOffsetPerClick()*((activeRange.getDistanceToTarget()/3)/activeScope.getDistanceForAdjust());

			 //translate the inches into clicks
			 clicksX = (int)(inchesDevX/clickEffectDistance);
			 clicksY = (int)(inchesDevY/clickEffectDistance);
		 }	
		r = new Result(clicksX, clicksY, horizontalRotation, verticalRotation);
	}
	
	public Result getResult(){
		return r;
	}
	
	/**
	 * All management of the savables list happens here.
	 * Ensures there is always a generic scope and range OR
	 * a user supplied scope and range, and one is active
	 * - if newSavable == existing, delete existing
	 * - if newSavable.getName().equals(existing.getName()), overwrite existing
	 * - if newSavable is not in list, add it 
	 * Finally, calls save & load.
	 * @param newSavable
	 */
	public void updateSavables(Savable newSavable){
		
		boolean updated = false;
		
		Range newRange;
		Range oldRange;					
				
		if(newSavable instanceof Range){                //is this Savable a Scope or Range?
			newRange = (Range)newSavable;			  			
			for (Savable s:savables){              
				if(s instanceof Range){           
					oldRange = (Range)s;              //cast the existing savable to so we can use its .equals()
					if(newRange.equals(oldRange)){    //if exactly the same, delete.
						updated = true;
						savables.remove(s); 
						break;
					}
					else if(newRange.getName().equals(oldRange.getName())){    //if names == but attributes !=, it's an update call
						updated = true;
						savables.remove(s);                           //delete and replace the existing Range
						savables.add(newSavable); 
						setActiveRange(s.getName());
						break;
					}
				}
			}							
		}
	
		Scope newScope;
		Scope oldScope;
		
		if(newSavable instanceof Scope){                //is newSavable a Scope?
			newScope = (Scope)newSavable;            				
			for (Savable s:savables){                				
				if(s instanceof Scope){           //is current savable a scope?				
					oldScope = (Scope)s;              //cast the existing savable to so we can use its .equals()
					if(newScope.equals(oldScope)){    //if exactly the same, delete.
						updated = true;						
						savables.remove(s);       
						break;
					}                             
					else if(newScope.getName().equals(oldScope.getName())){    //if names == but attributes !=, it's an update call
						updated = true;
						savables.remove(s);                           //delete and replace the old Scope
						savables.add(newSavable);
						setActiveScope(s.getName());
						break;
					}						
				}
			}
		}
		if(updated == false){			//if none of the above actions occurred, it's a new Scope or Range
			savables.add(newSavable);
			if(newSavable instanceof Range)
				setActiveRange(newSavable.getName());
			else
				setActiveScope(newSavable.getName());		
		}		
		
		ensurePopulated();    //one of each must exist
		ensureActives();	//one of each must be active			
		writeSavables();    //save 'em
		loadSavables();    	//reload 'em	
	}
	
	/**
	 * If the last scope or range has been deleted, 
	 * adds a generic one to populate the spinners.
	 */
	public void ensurePopulated(){

		ArrayList<Range> rangeList = getRanges();						
		if(rangeList.size() == 0){                                         //if there are no ranges yet, add a generic one to savables	
			savables.add(new Range(0,0,"Add a Range"));
			setActiveRange("Add a Range");                                 //it's the only one, make it active
		}			
		
		if(rangeList.size() > 1){                                         //if user Ranges exist, delete it.
			Range genericRange = new Range(0,0,"Add a Range");
			for (Savable sa : savables){
				if (sa instanceof Range){
					if(((Range)sa).equals(genericRange)){   
						savables.remove((Range)sa); 
						break;
					}
				}
			}
		}
		//same for Scopes
		ArrayList<Scope> scopeList = getScopes();		
						
		if(scopeList.size() == 0){           //if no scopes exist, add the temp                     
			Scope genericScope = new Scope("Add a Scope", 0, 0, true, true);
			savables.add(genericScope);
		}			
		
		if(scopeList.size() > 1){          //if user scopes exist, delete the temp scope
			Scope genericScope = new Scope("Add a Scope", 0, 0, true, true);
			for (Savable sa : savables){
				if (sa instanceof Scope){
					if(((Scope)sa).equals(genericScope)){   //if one of the ranges is generic...
						savables.remove((Scope)sa); //delete it from global savables
						break;
					}
				}
			}
		}			
	}
	
	private void ensureActives(){
		//Check to see if any scope or range is active. If not, set first range and first scope = active.
		boolean hadActiveScope = false;
		boolean hadActiveRange = false;
		for (Savable s: savables){           //are there actives already?
			if(s.getActiveStatus()){
				if(s instanceof Scope)
					hadActiveScope = true;
				else
					hadActiveRange = true;				
			}
		}		
		if(!hadActiveScope){            //if not active scope exists
			for(Savable s:savables){
				if(s instanceof Scope){   //set the first one we see to active
					s.setActiveStatus(true);
					break;
				}
			}
		}
		if(!hadActiveRange){             //if no active range exists
			for(Savable s:savables){
				if(s instanceof Range){     //set the first one we see to active
					s.setActiveStatus(true);
					break;
				}
			}
		}				
	}
	/**
	 * Writes scopes and ranges to external storage
	 */
	private void writeSavables(){
				
		try
	    {   
			File existingToDelete = new File(Environment.getExternalStorageDirectory(), "savables.ser");
            existingToDelete.delete();
			
			FileOutputStream fileOut = getApplicationContext().openFileOutput("savables.ser", MODE_PRIVATE);
			
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
	        														
	        out.writeObject(savables);
	        out.flush(); 											
	        out.close(); 											
		    fileOut.close();			
	    }
		catch(IOException i){
			if(i instanceof FileNotFoundException){}
			else
				i.printStackTrace();			
	    }
	  }
		
	@SuppressWarnings("unchecked")
	private void loadSavables() {

		savables.clear();
	    try{
	    	FileInputStream fileIn = openFileInput("savables.ser");
	    	ObjectInputStream in = new ObjectInputStream(fileIn);
	        savables = (ArrayList<Savable>)in.readObject();
	        in.close();
	        fileIn.close();    		    	
	    }
	    catch(IOException i){
	         i.printStackTrace();
	         return;
	    }
	    catch(ClassNotFoundException c){
	         c.printStackTrace();
	         return;
	    }	     
	}
	
	/**
	 * Processes each savable, converting its values to metric. 
	 */
	public void convertSavablesToMetric(){
		for (Savable s: savables){
			if (s instanceof Range){
				((Range)s).setDistanceToTarget((((Range)s).getDistanceToTarget()*.3048f));  //convert feet to meters
				((Range)s).setTargetDiameter((((Range)s).getTargetDiameter()*2.54f));  //convert inches to centimeters
				//s.setUnitsMetric();
			}
			else if(s instanceof UnitStatus)
				((UnitStatus)s).setUnitsMetric();
			else{
				((Scope)s).setOffsetPerClick((((Scope)s).getOffsetPerClick()*2.54));   //convert inches to centimeters
				((Scope)s).setDistanceForAdjust((((Scope)s).getDistanceForAdjust()*.9144));   //convert yards to meters				
				//s.setUnitsMetric();				
			}
		}
		writeSavables();    
		loadSavables(); 
	}
	/**
	 * Processes each savable, converting its values to imperial. 
	 */
	public void convertSavablesToImperial(){
		for (Savable s: savables){
			if (s instanceof Range){
				((Range)s).setDistanceToTarget((((Range)s).getDistanceToTarget()/.3048f));  //convert meters to feet
				((Range)s).setTargetDiameter((((Range)s).getTargetDiameter()/2.54f));  //convert centimeters to inches 
				//s.setUnitsImperial();
			}
			else if(s instanceof UnitStatus)
				((UnitStatus)s).setUnitsImperial();
			else{
				((Scope)s).setOffsetPerClick((((Scope)s).getOffsetPerClick()/2.54));   //convert centimeters to inches
				((Scope)s).setDistanceForAdjust((((Scope)s).getDistanceForAdjust()/.9144));   //convert meters to yards				
				//s.setUnitsImperial();				
			}
		}
		writeSavables();    
		loadSavables(); 
	}
}