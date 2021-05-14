package com.galere.pictures.config.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.galere.pictures.config.ApplicationEndHandle;
import com.galere.pictures.services.IEncryptionService;

/**
 * <b>
 * 	Thread permettant la mise à jour de la clé de chiffrement. Utilisé dans la class ApplicationEndHandle.
 * </b>
 * <p>
 * 	Cette classe contient un lien vers le service responsable du chiffrement/déchiffrement. Une fois lancé, 
 * 	chaque heure, ce thread appelera la méthode reloadKey() du service IEncryptionService. Le début et la 
 * 	fin de ce processus sont marqués par des logs dans la console.
 * </p>
 * <p>
 * 	Pour patienter une heure, ce thread fera plusieurs appels à la méthode sleep() de une seconde (1000 ms).
 * 	Si à un moment la variable 'continu' est passée à false, grâce à la méthode end(), alors ce thread 
 * 	s'arrêtera après son dernier appel à la méthode sleep().
 * </p>
 * 
 * @see ApplicationEndHandle
 * @see IEncryptionService#reloadKey()
 * @see ReloadKeyThread#run()
 * @see ReloadKeyThread#end()
 * @see ReloadKeyThread#sleep(long)
 * 
 * @author Ilias HATTANE
 * @version 1.0
 *
 */
public class ReloadKeyThread extends Thread {
	
	/**
	 * <b> Logger de la class ReloadKeyThread. </b>
	 */
	private final static Logger log = LoggerFactory.getLogger(ReloadKeyThread.class);
	
	/**
	 * <b> Variable permettant de savoir si ce thread doit être actif ou s'arrêter. </b>
	 * 
	 * @see ReloadKeyThread#run()
	 * @see ReloadKeyThread#end()
	 */
	private boolean continu = true;
	
	/**
	 * <b> Variable contenant le temps en millisecondes à attendre entre chaque mise à jour de la clé de chiffrement. </b>
	 *		
	 * @see ReloadKeyThread#run()
	 */
	private final long SKIP = 1000 * 60 * 60;
	
	/**
	 * <b> Implémentation du service IEncryptionService. </b>
	 */
	private IEncryptionService encryption;
	
	/**
	 * <b> Constructeur de la class, nécessite une implémentation de l'interface IEncryptionService. </b>
	 * 
	 * @param service Une implémentation de la class IEncryptionService.
	 */
	public ReloadKeyThread(IEncryptionService service) {
		encryption = service;
	}
	
	/**
	 * <b> Lance ce thread. </b>
	 * 
	 * <p> Débute par mettre à jour la clé de chiffrement, puis attends une heure, puis recommence. </p>
	 * 
	 * <p> Si à un moment la méthode end() est appelée, ce thread s'arrêtera de lui même. </p>
	 */
	@Override
	public void run() {
		try {
			while (continu) {
				log.info("START RELOADING KEY");
				encryption.reloadKey();
				log.info("END OF KEY RELOADING");
				for (int i = 0; i < SKIP/1000 && continu; i++) {
					if (!continu)
						break;
					sleep(1000); 
				}
			}
		} catch (Exception e) {e.printStackTrace();}
		log.info("Thread Ended");
	}
	
	/**
	 * <b> Méthode permettant d'arrêter ce thread. </b>
	 */
	public void end() {
		log.info("End of reloadKeyThread...");
		continu = false;
		log.info("OK");
	}
	
}
