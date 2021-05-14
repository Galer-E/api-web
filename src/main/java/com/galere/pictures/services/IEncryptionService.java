package com.galere.pictures.services;

/**
 * <b>
 * 	Interface du service permettant de gérer le chiffrement et le déchiffrement.
 * </b>
 * 
 * @author Ilias HATTANE
 * @version 1.0
 *
 */
public interface IEncryptionService {
	
	/**
	 * <b> Permet de déchiffrer le texte passé en paramètre. </b>
	 * 
	 * @param text Le texte à déchiffrer.
	 * @return Le texte déchiffré.
	 * @throws Exception En cas d'erreur.
	 */
	public String decrypt(String text) throws Exception;
	
	/**
	 * <b> Permet de chiffrer le texte passé en paramètre. </b>
	 * 
	 * @param text Le texte à chiffrer.
	 * @return Le texte chiffré.
	 * @throws Exception En cas d'erreur.
	 */
	public String encrypt(String text) throws Exception;
	
	/**
	 * <b> Permet de mettre à jour la clé de chiffrement. </b>
	 */
	public void reloadKey();
	
	/**
	 * <b> Permet de charger la clé de chiffrement initiale et de rechiffrer les mots de passes en conséquence. </b>
	 */
	public void loadInitialKey();
	
	/**
	 * <b> Méthode au comportement différent selon la version, ne pas utiliser en générale. </b>
	 */
	public void test();
	
}
