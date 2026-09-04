import SwiftUI
import SharedLogic

struct ContentView: View {
    @StateObject private var viewModel: ObservableStudyViewModel

    init(dataStore: SrsDataStore) {
        _viewModel = StateObject(wrappedValue: ObservableStudyViewModel(dataStore: dataStore))
    }

    var body: some View {
        NavigationStack {
            VStack(spacing: 16) {
                createDeckSection
                deckListSection
            }
            .padding()
            .navigationTitle("Study")
            .toolbar {
                ToolbarItem(placement: .topBarTrailing) {
                    Button {
                        viewModel.refresh()
                    } label: {
                        Image(systemName: "arrow.clockwise")
                    }
                    .disabled(viewModel.state.isLoading)
                    .accessibilityLabel("Refresh decks")
                }
            }
        }
        .task {
            viewModel.start()
        }
        .onDisappear {
            viewModel.stop()
        }
    }

    private var createDeckSection: some View {
        VStack(alignment: .leading, spacing: 8) {
            TextField(
                "New deck name",
                text: Binding(
                    get: { viewModel.state.deckName },
                    set: { viewModel.deckNameChanged($0) }
                )
            )
            .textFieldStyle(.roundedBorder)
            .submitLabel(.done)
            .onSubmit {
                viewModel.createDeck()
            }

            if let deckNameError = viewModel.state.deckNameError {
                Text(deckNameError)
                    .font(.footnote)
                    .foregroundStyle(.red)
            }

            if let errorMessage = viewModel.state.errorMessage {
                Text(errorMessage)
                    .font(.footnote)
                    .foregroundStyle(.red)
            }

            Button {
                viewModel.createDeck()
            } label: {
                if viewModel.state.isCreatingDeck {
                    ProgressView()
                } else {
                    Label("Create Deck", systemImage: "plus")
                }
            }
            .buttonStyle(.borderedProminent)
            .disabled(viewModel.state.isCreatingDeck)
        }
    }

    @ViewBuilder
    private var deckListSection: some View {
        if viewModel.state.isLoading {
            ProgressView("Loading decks...")
                .frame(maxWidth: .infinity, maxHeight: .infinity)
        } else if viewModel.state.decks.isEmpty {
            ContentUnavailableView(
                "No Decks Yet",
                systemImage: "rectangle.stack.badge.plus",
                description: Text("Create a deck to start studying.")
            )
        } else {
            List(viewModel.state.decks, id: \.idDescription) { deck in
                Label(deck.name, systemImage: "rectangle.stack")
            }
            .listStyle(.plain)
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView(dataStore: SrsDataStore(driverFactory: IosDatabaseDriverFactory()))
    }
}
