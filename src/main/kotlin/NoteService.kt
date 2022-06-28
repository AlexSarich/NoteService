class NoteService {
    val notes = mutableListOf<Note>()
    var comments = mutableListOf<Comment>()
    val deletedComments = mutableListOf<Comment>()

    var noteId = 0u
    var commentId = 0u

    fun addNote(note: Note, title: String, text: String): UInt {
        noteId++
        notes.add(note.copy(nid = noteId, title = title, text = text))
        return notes.last().nid
    }

    fun addComment(comment: Comment, message: String): Comment {
        if (findNoteById(comment.noteId)) {
            commentId++
            comments.add(comment.copy(cid = commentId, message = message))
            return comments.last()
        } else {
            throw NoteNotFindException("Записки нет")
        }
    }

    fun deleteNote(note: Note): Boolean {
        notes.remove(note)
        return true
    }

    fun deleteComment(comment: Comment): Boolean {
        if (findCommentById(comment.cid)) {
            deletedComments.add(comment)
            comments.remove(comment)
            return true
        } else {
            throw CommentNotFindException("Коммента нет")
        }
    }

        fun editNote(noteUpd: Note): Boolean {
        for ((index, note) in notes.withIndex()) {
            if (noteUpd.nid == note.nid) {
                notes[index] = noteUpd.copy(nid = note.nid)
                return true
            }
        }
        return false
    }

    fun editComment(commentUpd: Comment): Boolean {
        for ((index, comment) in comments.withIndex()) {
            if (commentUpd.cid == comment.cid) {
                comments[index] = commentUpd.copy(cid = comment.cid)
                return true
            }
        }
        return false
    }

    fun getNotes(userId: UInt): List<Note> {
        val usersNotes = emptyList<Note>().toMutableList()
        for (note in notes) {
            if (userId == note.userId) usersNotes += note
        }
        return usersNotes.ifEmpty {
            throw NoteNotFindException("Записки нет")
        }
    }

    fun getNoteById(noteId: UInt): Note {
        for (note in notes) {
            if (note.nid == noteId) {
                return note
            }
        }
        throw NoteNotFindException("Записки нет")
    }

    fun getComments(note: Note): List<Comment> {
        val getComments = emptyList<Comment>().toMutableList()
        for (comment in comments) {
            if (note.nid == comment.noteId) getComments += comment
        }
        return getComments
    }

    fun restoreComment(restoringComment: Comment): Boolean {
        val iterator = deletedComments.iterator()
        while (iterator.hasNext()) {
            val item = iterator.next()
            if (item.cid == restoringComment.cid) {
                iterator.remove()
                comments.add(item)
                comments = comments.sortedWith(compareBy { it.cid }).toMutableList()
                return true
            }
        }
        throw CommentNotFindException("Коммента нет")
    }


    fun findNoteById(noteId: UInt): Boolean {
        for (note in notes) {
            if (note.nid == noteId) {
                return true
            }
        }
        return false
    }

    fun findCommentById(commentId: UInt): Boolean {
        for (comment in comments) {
            if (comment.cid == commentId) {
                return true
            }
        }
        return false
    }
}

class NoteNotFindException(message: String) : RuntimeException()

class CommentNotFindException(message: String) : RuntimeException()