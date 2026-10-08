/**
 * The registry of beneficiaries: who each one is, by the legal document identifying them, and where
 * they live. Other systems of the program read it; nothing writes to it through the API.
 */
@org.springframework.modulith.ApplicationModule(displayName = "Registry")
package lat.soulware.beneficiarios.registry;
