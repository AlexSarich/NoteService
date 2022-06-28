import org.junit.Test

import org.junit.Assert.*

class NoteServiceTest {

    @Test
    fun addNote_check_id() {
        val service = NoteService()
        val unexpectedResult = 0
        assertNotEquals(unexpectedResult, service.addNote(Note(), "Новая заметка", "Текст"))
    }

    @Test
    fun createComment_should_create() {
        val service = NoteService()
        val commentsSizeBefore = service.comments.size
        service.addNote(Note(), "Новая заметка", "Текст")
        service.addComment(Comment(noteId = 1u), "Новый коммент")
        val commentsSizeAfter = service.comments.size
        assertTrue(commentsSizeAfter == commentsSizeBefore + 1)
    }

    @Test(expected = NoteNotFindException::class)
    fun create_comment_should_throw() {
        val service = NoteService()
        service.addNote(Note(), "Новая заметка", "Текст")
        service.addComment(Comment(noteId = 20u), "Новый коммент")
    }

    @Test
    fun deleteComment_Should_delete() {
        val service = NoteService()
        val note = service.addNote(Note(), "Новая заметка", "Текст")
        val comment = service.addComment(Comment(noteId = 1u),"Текст")
        val commentsSizeBefore = service.comments.size
        val deletedCommentsSizeBefore = service.deletedComments.size
        service.deleteComment(comment)
        val commentsSizeAfter = service.comments.size
        val deletedCommentsSizeAfter = service.deletedComments.size
        assertTrue(commentsSizeAfter == commentsSizeBefore - 1 && deletedCommentsSizeAfter == deletedCommentsSizeBefore + 1)
    }

    @Test(expected = CommentNotFindException::class)
    fun deleteComment_Should_throw() {
        val service = NoteService()
        service.deleteComment(Comment())
    }

    @Test
    fun editNote_Existing() {
        val service = NoteService()
        val note = service.addNote(Note(), "Новая заметка", "Текст")
        val noteUpd = Note(nid = 1u, text = "Новый текст")
        val result = service.editNote(noteUpd)
        assertTrue(result)
    }

    @Test
    fun editNote_UnExisting() {
        val service = NoteService()
        val note = service.addNote(Note(),"Новая заметка", "Текст")
        val noteUpd = Note(nid = 2u, text = "Новый текст")
        val result = service.editNote(noteUpd)
        assertFalse(result)
    }

    @Test
    fun editComment_Existing() {
        val service = NoteService()
        val note = service.addNote(Note(), "Новая заметка", "Текст")
        val comment = service.addComment(Comment(noteId = 1u),"Текст")
        val commentUpd = Comment(cid = 1u, noteId = 1u, message = "Новый текст")
        val result = service.editComment(commentUpd)
        assertTrue(result)
    }

    @Test
    fun editComment_UnExisting() {
        val service = NoteService()
        val note = service.addNote(Note(), "Новая заметка", "Текст")
        val comment = service.addComment(Comment(noteId = 1u), "Текст")
        val commentUpd = Comment(cid = 4u, noteId = 1u, message = "Новый текст")
        val result = service.editComment(commentUpd)
        assertFalse(result)
    }

    @Test
    fun getNotes_should_get() {
        val service = NoteService()
        val note1 = service.addNote(Note(nid = 1u, userId = 10u), "Новая заметка", "Текст")
        val note2 = service.addNote(Note(nid = 2u, userId = 10u), "Новая заметка", "Текст")
        val expectedResult = mutableListOf(service.getNoteById(note1), service.getNoteById(note2))
        val result = service.getNotes(10u)
        assertEquals(expectedResult, result)
    }

    @Test(expected = NoteNotFindException::class)
    fun getNotes_should_throw() {
        val service = NoteService()
        val note1 = service.addNote(Note(userId = 10u), "Новая заметка", "Текст")
        val note2 = service.addNote(Note(userId = 10u), "Новая заметка", "Текст")
        service.getNotes(12u)
    }

    @Test
    fun getNoteById_Should_get() {
        val service = NoteService()
        val note1 = service.addNote(Note(), "Новая заметка", "Текст")
        val note2 = service.addNote(Note(), "Новая заметка", "Текст")
        val expectedResult = note2
        val result = service.getNoteById(2u).nid
        assertEquals(expectedResult, result)
    }

    @Test(expected = NoteNotFindException::class)
    fun getNoteById_should_throw() {
        val service = NoteService()
        val note1 = service.addNote(Note(), "Новая заметка", "Текст")
        val note2 = service.addNote(Note(), "Новая заметка", "Текст")
        service.getNoteById(160u)
    }

    @Test
    fun getComments_Should_get() {
        val service = NoteService()
        val note = service.addNote(Note(), "Новая заметка", "Текст")
        val getNote = service.getNoteById(note)
        val comment1 = service.addComment(Comment(noteId = 1u), "Текст")
        val comment2 = service.addComment(Comment(noteId = 1u), "Текст")
        val expectedResult = mutableListOf(comment1, comment2)
        val result = service.getComments(getNote)
        assertEquals(expectedResult, result)
    }

    @Test
    fun restoreComment_Should_Restore() {
        val service = NoteService()
        val note1 = service.addNote(Note(), "Новая заметка", "Текст")
        val comment1 = service.addComment(Comment(noteId = 1u), "Текст")
        val comment2 = service.addComment(Comment(noteId = 1u), "Текст")
        service.deleteComment(comment1)
        assertTrue(service.restoreComment(comment1))
    }

    @Test(expected = CommentNotFindException::class)
    fun restoreComment_Should_throw() {
        val service = NoteService()
        val note1 = service.addNote(Note(), "Новая заметка", "Текст")
        val comment1 = service.addComment(Comment(noteId = 1u), "Текст")
        service.restoreComment(comment1)
    }
}