Editar o ficheiro config.properties que se encontra na pasta "bin" para mudar as configurações do programa (Peer). 

Os triggers "java proj1.Client" utilizam o mesmo ficheiro config.properties para iniciar.



Correr Peer: 
		
		CMD: 
				java -cp ..\lib\sqlite.jar; proj1.Peer
		
		Eclipse: 
				
				Correr Peer.java
				
				
				
Enviar comandos:

		CMD:
		
			java -cp ..\lib\sqlite.jar; proj1.Client backup <filepath> <replicationdeg>
			java -cp ..\lib\sqlite.jar; proj1.Client restore <filepath>
			java -cp ..\lib\sqlite.jar; proj1.Client delete <filepath>
			
			
			
Existe ainda uma funcionalidade para fazer Merge dos chunks e obter o ficheiro original: 

		CMD No diretório bin:
		
			java utils.Merge <filepath>
			
				Exemplo: 
				
					java utils.Merge restored/ffd32f8f784ff65d42e2b402ac27458017ded124c90625a00cc9fd13ed8214de
					
						Irá criar um novo ficheiro na pasta dos chunks com o nome merge. Para o ver basta mudar a extensão para a original. 
		
			
	
