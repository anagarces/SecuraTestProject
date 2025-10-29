-- VERIFICAR DATOS INSERTADOS EN TABLA DE CUESTIONARIOS

-- Listar quizzes publicados
SELECT id, title, description FROM quiz WHERE is_published = 1;

-- Ver preguntas del quiz
SELECT qu.id AS quiz_id, q.id AS question_id, q.ord, q.text
FROM quiz qu
JOIN question q ON q.quiz_id = qu.id
WHERE qu.title = 'Demo: Seguridad básica'
ORDER BY q.ord;

-- Ver opciones por pregunta (incluye is_correct para verificar, ¡no lo expongas en el front!)
SELECT q.text AS pregunta, o.ord, o.text AS opcion, o.is_correct
FROM question q
JOIN option_item o ON o.question_id = q.id
JOIN quiz z ON z.id = q.quiz_id
WHERE z.title = 'Demo: Seguridad básica'
ORDER BY q.ord, o.ord;
