import Foundation
import SharedLogic

@MainActor
final class ObservableStudyViewModel: ObservableObject {
    @Published private(set) var state: StudyState

    private let sharedViewModel: StudyViewModel
    private let stateFlow: SkieSwiftStateFlow<StudyState>
    private var stateTask: Task<Void, Never>?

    init(dataStore: SrsDataStore) {
        let sharedViewModel = StudyViewModel(
            deckRepository: dataStore.decks,
            newDeckFactory: IOSNewDeckFactory()
        )
        let stateFlow = SkieSwiftStateFlow<StudyState>(sharedViewModel.state)

        self.sharedViewModel = sharedViewModel
        self.stateFlow = stateFlow
        self.state = stateFlow.value
    }

    func start() {
        guard stateTask == nil else { return }

        stateTask = Task { [stateFlow] in
            for await state in stateFlow {
                self.state = state
            }
        }

        refresh()
    }

    func stop() {
        stateTask?.cancel()
        stateTask = nil
    }

    func deckNameChanged(_ name: String) {
        sharedViewModel.onDeckNameChanged(name: name)
    }

    func createDeck() {
        Task { try? await sharedViewModel.createDeck() }
    }

    func refresh() {
        Task { try? await sharedViewModel.refresh() }
    }
}
