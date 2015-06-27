package de.norvos;

import java.io.IOException;
import java.util.Scanner;

import org.whispersystems.libaxolotl.util.guava.Optional;
import org.whispersystems.textsecure.api.TextSecureAccountManager;
import org.whispersystems.textsecure.api.TextSecureMessageSender;
import org.whispersystems.textsecure.api.push.TrustStore;

import de.norvos.log.Errors;
import de.norvos.log.Errors.Message;
import de.norvos.store.ConfigStore;
import de.norvos.store.DurableStoreManager;
import de.norvos.store.NorvosTrustStore;
import de.norvos.store.axolotl.NorvosAxolotlStore;

public class Main {


	public static void main(String[] args) {
		if (!DurableStoreManager.availableOnDisk()) {
			DurableStoreManager.createNewAxolotlStore();
			registerThisClient();
		}
	}

	private static void registerThisClient() {

		Scanner reader = new Scanner(System.in);
		System.out.println("Enter your telephone number in international format:");
		
		ConfigStore.setUsername(reader.nextLine());
		
		TextSecureAccountManager accountManager =
				new TextSecureAccountManager(ConfigStore.getURL(), new NorvosTrustStore(), ConfigStore.getUsername(),
						ConfigStore.getPassword());

		
		accountManager.requestSmsVerificationCode();
				

		System.out.println("You will now receive an SMS containing your verification code. Please enter it here:");
		
		String receivedVerificationCode = reader.nextLine();
		
		
		accountManager.verifyAccount(receivedVerificationCode, generateRandomSignalingKey(), false,
				generateRandomInstallId());
		// accountManager.setGcmId(Optional.of(GoogleCloudMessaging.getInstance(this).register(REGISTRATION_ID)));
		accountManager.setPreKeys(DurableStoreManager.getAxolotlStore().getIdentityKeyPair().getPublicKey(), DurableStoreManager.getAxolotlStore().getLastResortKey(),
				DurableStoreManager.getAxolotlStore().getSignedPreKey(), DurableStoreManager.getAxolotlStore().getOneTimePreKeys());
	}
}
