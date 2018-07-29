package com.umbcapp.gaurk.snapeve;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;

/**
 * Created by Amey on 26-06-2018.
 */

public class AzureConfiguration {

    public static String SenderId = "249087334030";
    public static String HubName = "snapeve";
    public static String HubListenConnectionString = "Endpoint=sb://snapevenamespace.servicebus.windows.net/;SharedAccessKeyName=DefaultFullSharedAccessSignature;SharedAccessKey=zfLbNpg6rU4784821SEtUtfUJaWe5frTtCHHDSSfLA8=";

    public static String containerName = "snapeve";
    public static String storageConnectionString = "DefaultEndpointsProtocol=https;AccountName=snapeve;AccountKey=ljrXw4tcxVAdPRgr6Q3aqKX//qYuTmc6P7lKpK44Fm7yyRZvVfvSk1023BlUtB2nTARmvXrp5JfR+YJfiXQ5Og==;EndpointSuffix=core.windows.net";
    public static String Storage_url = "https://snapeve.blob.core.windows.net/snapeve/";

    public static CloudBlobContainer getContainer() throws Exception {
        // Retrieve storage account from connection-string.

        CloudStorageAccount storageAccount = CloudStorageAccount.parse(AzureConfiguration.storageConnectionString);

        // Create the blob client.
        CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
        // Get a reference to a container.
        // The container name must be lower case
        CloudBlobContainer container = blobClient.getContainerReference(AzureConfiguration.containerName);

        return container;
    }

}
