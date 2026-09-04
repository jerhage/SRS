import SwiftUI
import SharedLogic

struct ContentView: View {
    @StateObject private var viewModel: StudyScreenViewModel

    init(dataStore: SrsDataStore) {
        _viewModel = StateObject(wrappedValue: StudyScreenViewModel(deckRepository: dataStore.decks))
    }

    var body: some View {
        NavigationStack {
            VStack(spacing: 16) {
                createDeckSection

                Group {
                    if viewModel.isLoading {
                        ProgressView("Loading decks...")
                            .frame(maxWidth: .infinity, maxHeight: .infinity)
                    } else if viewModel.decks.isEmpty {
                        ContentUnavailableView(
                            "No Decks Yet",
                            systemImage: "rectangle.stack.badge.plus",
                            description: Text("Create a deck to start studying.")
                        )
                    } else {
                        List(viewModel.decks, id: \.idDescription) { deck in
                            Label(deck.name, systemImage: "rectangle.stack")
                        }
                        .listStyle(.plain)
                    }
                }
            }
            .padding()
            .navigationTitle("Study")
            .toolbar {
                ToolbarItem(placement: .topBarTrailing) {
                    Button {
                        Task { await viewModel.refresh() }
                    } label: {
                        Image(systemName: "arrow.clockwise")
                    }
                    .disabled(viewModel.isLoading)
                    .accessibilityLabel("Refresh decks")
                }
            }
        }
        .task {
            await viewModel.refresh()
        }
    }

    private var createDeckSection: some View {
        VStack(alignment: .leading, spacing: 8) {
            TextField("New deck name", text: $viewModel.deckName)
                .textFieldStyle(.roundedBorder)
                .submitLabel(.done)
                .onSubmit {
                    Task { await viewModel.createDeck() }
                }

            if let deckNameError = viewModel.deckNameError {
                Text(deckNameError)
                    .font(.footnote)
                    .foregroundStyle(.red)
            }

            if let errorMessage = viewModel.errorMessage {
                Text(errorMessage)
                    .font(.footnote)
                    .foregroundStyle(.red)
            }

            Button {
                Task { await viewModel.createDeck() }
            } label: {
                if viewModel.isCreatingDeck {
                    ProgressView()
                } else {
                    Label("Create Deck", systemImage: "plus")
                }
            }
            .buttonStyle(.borderedProminent)
            .disabled(viewModel.isCreatingDeck)
        }
    }
}

@MainActor
final class StudyScreenViewModel: ObservableObject {
    @Published var decks: [Deck_] = []
    @Published var deckName = ""
    @Published var isLoading = false
    @Published var isCreatingDeck = false
    @Published var deckNameError: String?
    @Published var errorMessage: String?

    private let deckRepository: DeckRepository

    init(deckRepository: DeckRepository) {
        self.deckRepository = deckRepository
    }

    func refresh() async {
        isLoading = true
        errorMessage = nil

        do {
            decks = try await deckRepository.getAll(includeArchived: false)
        } catch {
            errorMessage = "Could not load decks."
        }

        isLoading = false
    }

    func createDeck() async {
        let name = deckName.trimmingCharacters(in: .whitespacesAndNewlines)
        guard !name.isEmpty else {
            deckNameError = "Enter a deck name."
            return
        }

        isCreatingDeck = true
        deckNameError = nil
        errorMessage = nil

        do {
            let now = Int64(Date().timeIntervalSince1970 * 1_000)
            let deck = Deck_(
                id: UUID().uuidString,
                name: name,
                parentId: nil,
                createdAt: now,
                updatedAt: now,
                isArchived: false
            )

            try await deckRepository.save(deck: deck)
            decks = try await deckRepository.getAll(includeArchived: false)
            deckName = ""
        } catch {
            errorMessage = "Could not create deck."
        }

        isCreatingDeck = false
    }
}

private extension Deck_ {
    var idDescription: String {
        String(describing: id)
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView(dataStore: SrsDataStore(driverFactory: IosDatabaseDriverFactory()))
    }
}
