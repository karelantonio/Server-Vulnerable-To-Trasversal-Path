import java.io.*;
import com.sun.net.httpserver.*;
import java.net.*;

public class Main
{
	public static HttpServer server = null;
	public static RelPath file = null;
	
	/**
	* Metodo principal que ejecutará nuestra aplicación
	*/
	public static void main(String[] args) throws Exception
	{
		System.out.println("Creando servidor en: http://localhost:8080...");
		
		//Creamos el archivo donde buscaremos los otros necesarios por el usuario/cliente
		file=new RelPath(new File(System.getProperty("java.io.tmpdir", null)));
		
		//Ahora creamos el servidor y le agregamos nuestro FileHandler (el contexto que listará los archivos
		server=HttpServer.create(new InetSocketAddress("localhost", 8080), 8080);
		server.createContext("/", new FileHandler());
		
		System.out.println("Iniciando servidor...");
		//Lo iniciamos
		server.start();
		System.out.println("Inicio exitoso!");
		
		System.out.println("El servidor se apagará automaticamente en 240 segundos (4 minutos)");
		Thread.sleep(240*1000);
	}
}
