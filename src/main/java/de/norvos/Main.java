package de.norvos;

import java.io.IOException;
import java.util.Scanner;

import org.whispersystems.libaxolotl.util.guava.Optional;
import org.whispersystems.textsecure.api.TextSecureAccountManager;
import org.whispersystems.textsecure.api.TextSecureMessageSender;
import org.whispersystems.textsecure.api.push.TrustStore;

import de.norvos.account.Registrator;
import de.norvos.account.ServerAccount;
import de.norvos.axolotl.NorvosAxolotlStore;
import de.norvos.axolotl.NorvosTrustStore;
import de.norvos.log.Errors;
import de.norvos.log.Errors.Message;
import de.norvos.persistence.DurableStoreManager;

public class Main {


	public static void main(String[] args) {
		if (!DurableStoreManager.availableOnDisk()) {
			DurableStoreManager.createNewAxolotlStore();
			Registrator.register(configuration, handler);
		}
	}
}