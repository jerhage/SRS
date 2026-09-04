import Foundation
import SharedLogic

final class IOSNewDeckFactory: NSObject, NewDeckFactory {
    func create(name: String) -> Deck_ {
        let now = Int64(Date().timeIntervalSince1970 * 1_000)

        return Deck_(
            id: UUID().uuidString,
            name: name,
            parentId: nil,
            createdAt: now,
            updatedAt: now,
            isArchived: false
        )
    }
}
