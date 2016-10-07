package axohEngine2.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/****************************************************************************************************
 * @author Travis R. Dewitt
 * @version 1.0
 *          Date: June 15, 2015
 *          *
 *          Title: Saving
 *          Description: This class is used to correctly serialize objects, specifically the 'Data.java' class
 *          It also creates new files when needed.
 *          <p>
 *          This work is licensed under a Attribution-NonCommercial 4.0 International
 *          CC BY-NC-ND license. http://creativecommons.org/licenses/by-nc/4.0/
 ****************************************************************************************************/

public class Save {

    private Data data;

    public Save(Data d) {
        data = d;
    }

    public void saveState(File file) {
    	try {
			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(data);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}