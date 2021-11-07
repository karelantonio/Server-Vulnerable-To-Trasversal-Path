#!/bin/bash
if [ $# -gt 0 ];then
	echo 'Demasiados argumentos, esta aplicación no necesita ninguno!'
	exit 1
fi

if [ -f server.zip.b64 ];then
	echo "Desempaquetando el servidor, espere un momento..."
	cat server.zip.b64 | base64 -d > server.zip || { echo "El archivo descargado no es correcto, por favor vuelva a descargar" ; exit 1 ; }
fi

if [ ! -f server.zip ];then
	echo 'El archivo "server.zip" no existe, asegurese de copiarlo al directorio actual'
	exit 1
fi

echo "Cargando, espere un momento..."
dalvikvm -cp server.zip Main
