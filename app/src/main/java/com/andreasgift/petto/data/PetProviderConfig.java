package com.andreasgift.petto.data;

import ckm.simple.sql_provider.UpgradeScript;
import ckm.simple.sql_provider.annotation.ProviderConfig;
import ckm.simple.sql_provider.annotation.SimpleSQLConfig;

@SimpleSQLConfig(
        name = PetContract.PROVIDER_NAME,
        authority = PetContract.AUTHORITY,
        database = PetContract.DATABASE_NAME,
        version = 1)
class PetProviderConfig implements ProviderConfig {

    @Override
    public UpgradeScript[] getUpdateScripts() {
        return new UpgradeScript[0];
    }
}
