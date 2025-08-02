object SdkHolder {
    private var sdk: SomeSdk? = null

    fun set(sdk: SomeSdk) {
        this.sdk = sdk
    }

    fun get(): SomeSdk = requireNotNull(sdk)

    fun process(parameters: SdkParameters) {
        // do something
    }
}
