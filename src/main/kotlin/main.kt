fun main() {
    val service = NoteService()
    val a = service.addNote(Note(), "Новая заметка", "Текст заметки")
    println(a)
    //println(service.deleteNote(service.notes[0]))

    val b = service.addComment(Comment(noteId = 1u), "Новый коммент")
    println(b)
    println(service.deleteComment(service.comments[1]))
}