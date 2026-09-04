import SwiftUI
import SharedLogic

@main
struct iOSApp: App {
    private let dataStore: SrsDataStore

    init() {
        dataStore = SrsDataStore(driverFactory: IosDatabaseDriverFactory())
    }

    var body: some Scene {
        WindowGroup {
            ContentView(dataStore: dataStore)
        }
    }
}
