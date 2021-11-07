
import com.sun.net.httpserver.*;
import java.io.*;

/**
* Contexto del servidor para listar archivos, aqui tambien esta nuestra vulnerabilidad, pues
* el sercidor no maneja a que archivo accede nuestro cliente
*/
public class FileHandler implements HttpHandler
{
	byte[] not_found = "El recurso requerido no existe en este servidor!".getBytes();

	/**
	* Maneja la transaccion web
	*/
	@Override
	public void handle(HttpExchange p1) throws IOException
	{
		//Epicentro de la vulnerabilidad
		File file = Main.file.relativeTo(p1.getRequestURI().getPath());
		
		if(!file.exists()){
			p1.getResponseHeaders().add("Content-Type", "text/html");
			p1.sendResponseHeaders(404, not_found.length);
			p1.getResponseBody().write(not_found);
			p1.getRequestBody().close();
			return;
		}
		
		if(file.isDirectory()){
			p1.getResponseHeaders().add("Content-Type", "text/html");
			
			byte[] listing = generateListing(file);
			
			p1.sendResponseHeaders(200, listing.length);
			p1.getResponseBody().write(listing);
			p1.getResponseBody().close();
			return;
		}
		
		if(file.isFile()){
			p1.sendResponseHeaders(200, file.length());
			
			FileInputStream in = new FileInputStream(file);
			byte[] data = new byte[in.available()];
			in.read(data);
			in.close();
			
			p1.getResponseBody().write(data);
			p1.getResponseBody().close();
			return;
		}
	}

	/**
	* Maneja el listado de los ficheros
	*/
	private byte[] generateListing(File file)
	{
		StringBuilder bld = new StringBuilder();
		bld.append("<h2>Listando archivos de: "+file.getName()+"<br/>");
		bld.append("<h3>Directorios:</h3><br/>");
		for(File tmp: file.listFiles(new FileListing(true))){
			bld.append("<a href=\""+tmp.getName()+"\">"+tmp.getName()+"/</a><br/>");
		}
		bld.append("<h3>Archivos:</h3><br/>");
		for(File tmp: file.listFiles(new FileListing(false))){
			bld.append("<a href=\""+tmp.getName()+"\">"+tmp.getName()+"</a><br/>");
		}
		
		return bld.toString().getBytes();
	}
	
	class FileListing implements FileFilter{
		public final boolean directories;

		public FileListing(boolean directories)
		{
			this.directories = directories;
		}

		@Override
		public boolean accept(File p1)
		{
			return p1.isDirectory()==directories;
		}
	}
}
