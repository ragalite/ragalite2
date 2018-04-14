package ragalite2.internal.serialization;

import java.io.IOException;
import java.io.Serializable;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileNotFoundException;

import java.util.function.Supplier;


/**
 * Utility class for serialization.
 */
public final class Serialization {

  // Don't let this class be instantiated.
  private Serialization() throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Can't instantiate this class!"); 
  };
  
	/**
	 * Serializes an object into the specified file.
	 * 
   * Directories will be created as necessary.
   *
   * If the file already exists, the file will be overwritten.
   *
   * Through this routine, a file can represent only one object.
   *
   * @param toSerialize Object to serialize
   * @param objectFilePath Path to the file to contain the object
   * @return {@code true} if the serialization is successful, and {@code false}
   *         otherwise
   * @throws IOException if an I/O error occurs
	 */
	public static boolean serializeOverwrite(Serializable toSerialize, String objectFilePath) 
    throws IOException {
    makeDirs: {
      File objectFile = new File(objectFilePath);

      // make sure path to file exists
      // getParentFile to not make the file itself a directory
      objectFile.getParentFile().mkdirs();
    }

    // Create the necessary streams - open the file, and then
    // start writing *objects* into the file.
		try (FileOutputStream   fos = new FileOutputStream  (objectFilePath);
		     ObjectOutputStream oos = new ObjectOutputStream(fos   )) {
			oos.writeObject(toSerialize); // serialize it!
		}

    return true; // success!
	}

	/**
	 * Serializes an object into the specified file.
	 * 
   * Directories will be created as necessary.
   *
   * If the file already exists, serialization will not occur.
   *
   * Through this routine, a file can represent only one object.
   *
   * @param toSerialize Object to serialize
   * @param objectFilePath Path to the file to contain the object
   * @return {@code true} if the serialization is successful, and {@code false}
   *         otherwise
   * @throws IOException if an I/O error occurs
	 */
	public static boolean serialize(Serializable toSerialize, String objectFilePath) 
    throws IOException {
    makeDirs: {
      File objectFile = new File(objectFilePath);
      if (objectFile.exists()) { // don't overwrite!
        return false;
      }

      // make sure path to file exists
      // getParentFile to not make the file itself a directory
      objectFile.getParentFile().mkdirs();
    }

    // Create the necessary streams - open the file, and then
    // start writing *objects* into the file.
		try (FileOutputStream   fos = new FileOutputStream  (objectFilePath);
		     ObjectOutputStream oos = new ObjectOutputStream(fos   )) {
			oos.writeObject(toSerialize); // serialize it!
		}

    return true; // success!
	}
  
  /**
   * Deserializes an object from the specified file.
   *
   * @param objectFilePath Path to the file containing the object
   * @return The object contained by the file
   * @throws IOException if an I/O error occurs
   * @throws ClassNotFoundException if the type of the read object
   *                                is not in the classpath
   * @throws FileNotFoundException if the file does not exist
   *                               or is otherwise unreadable
   */
	public static Object deserialize(String objectFilePath)
	                     throws IOException, ClassNotFoundException,
                              FileNotFoundException {

    // Create the necessary streams - open the file, and then
    // start reading *objects* from the file.
		try (FileInputStream   fis = new FileInputStream  (objectFilePath);
		     ObjectInputStream ois = new ObjectInputStream(fis   )) {
			return ois.readObject();
		}
		/*catch (Exception e) {
			throw e;
		}*/
		/*
		catch (FileNotFoundException f) {
			return null;
		}
		catch (IOException i) {
			i.printStackTrace();
			return null;
		}
		catch (ClassNotFoundException c) {
			System.out.println("Employee class not found");
			c.printStackTrace();
			return null;
		}*/
	}

  /**
   * Deserializes a object of asserted type, and returns a default object if
   * the deserialization fails.
   *
   * @param objectFilePath Path to the file containing the object
   * @param defaultGenerator Generator for default objects
   * @param classParam Asserted type
   * @return The object contained by the file, or a default object
   *         if the deserialization fails
   */
	public static <T> T deserializeOrDefault(String fromFileDir,
	                                         Supplier<T> defaultGenerator,
                                           Class<T> classParam) {
		try {
			return (T) deserialize(fromFileDir);

		}
		catch (Exception e) {
			e.printStackTrace();
			return defaultGenerator.get();
		}
	}
}
