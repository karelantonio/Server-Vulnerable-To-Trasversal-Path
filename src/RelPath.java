import java.io.*;
import java.util.*;

/**
* Clase que maneja las rutas relativas, aqui es donde se encuentra nuestra vulnerabilidad,
* porque nuestro servidor no controla muy bien a que archivos o rutas relativas accede el
* cliente web
*/
public class RelPath
{
	private File base;

	/**
	* Constructor por defecto
	*/
	public RelPath(File base)
	{
		this.base = base;
	}
	
	/**
	* Generamos un archivo relativo al actual (base)
	*/
	public File relativeTo(String rel0){
		String rel = trim(rel0.trim());
		
		File current = base;
		StringTokenizer tk = new StringTokenizer(rel, "/\\", false);
		
		while(tk.hasMoreTokens()){
			String elem = tk.nextToken();
			
			switch(elem){
				//Directorio superior
				case "..":
					if(current.getParent()!=null){
						current=current.getParentFile();
					}
					break;
				case "~":
					//Asumimos que el directorio "Base" es la ruta HOME
					current=base;
					break;
				case ".":
					//Este punto sirve para referenciar el directorio actual
					break;
				default:
					current=new File(current, elem);
					break;
			}
		}
		
		return current;
	}

	/**
	* Metodo interno para borrar los slaches ( / ó \ ) de los extremos
	*/
	private String trim(String rel0)
	{
		if(rel0.isEmpty())return rel0;
		
		if(rel0.startsWith("/")||rel0.startsWith("\\")){
			rel0=rel0.substring(1);
		}
		if(rel0.endsWith("/")||rel0.endsWith("\\")){
			rel0=rel0.substring(0, rel0.length()-1);
		}
		return rel0;
	}
}
