-- COMANDOS PARA INSERTAR DATOS EN LOS CUESTIONARIOS

INSERT INTO quiz (title, description, is_published)
VALUES ('Demo: Seguridad básica', 'Buenas prácticas de ciberseguridad', 1);
	

-- Pregunta 1
INSERT INTO question (quiz_id, text, ord)
SELECT q.id, '¿Qué es phishing?', 1
FROM quiz q
WHERE q.title = 'Demo: Seguridad básica'
ORDER BY q.id DESC
LIMIT 1;

-- Pregunta 2
INSERT INTO question (quiz_id, text, ord)
SELECT q.id, '¿Qué es 2FA?', 2
FROM quiz q
WHERE q.title = 'Demo: Seguridad básica'
ORDER BY q.id DESC
LIMIT 1;

-- Opciones para Pregunta 1
INSERT INTO option_item (question_id, text, is_correct, ord)
SELECT qu.id, 'Ataque por suplantación de identidad', 1, 1
FROM question qu
JOIN quiz q ON q.id = qu.quiz_id
WHERE q.title = 'Demo: Seguridad básica' AND qu.text = '¿Qué es phishing?'
ORDER BY qu.id DESC
LIMIT 1;

INSERT INTO option_item (question_id, text, is_correct, ord)
SELECT qu.id, 'Copia de seguridad', 0, 2
FROM question qu
JOIN quiz q ON q.id = qu.quiz_id
WHERE q.title = 'Demo: Seguridad básica' AND qu.text = '¿Qué es phishing?'
ORDER BY qu.id DESC
LIMIT 1;

INSERT INTO option_item (question_id, text, is_correct, ord)
SELECT qu.id, 'Antivirus', 0, 3
FROM question qu
JOIN quiz q ON q.id = qu.quiz_id
WHERE q.title = 'Demo: Seguridad básica' AND qu.text = '¿Qué es phishing?'
ORDER BY qu.id DESC
LIMIT 1;

-- Opciones para Pregunta 2
INSERT INTO option_item (question_id, text, is_correct, ord)
SELECT qu.id, 'Autenticación de dos factores', 1, 1
FROM question qu
JOIN quiz q ON q.id = qu.quiz_id
WHERE q.title = 'Demo: Seguridad básica' AND qu.text = '¿Qué es 2FA?'
ORDER BY qu.id DESC
LIMIT 1;

INSERT INTO option_item (question_id, text, is_correct, ord)
SELECT qu.id, 'Un tipo de malware', 0, 2
FROM question qu
JOIN quiz q ON q.id = qu.quiz_id
WHERE q.title = 'Demo: Seguridad básica' AND qu.text = '¿Qué es 2FA?'
ORDER BY qu.id DESC
LIMIT 1;

INSERT INTO option_item (question_id, text, is_correct, ord)
SELECT qu.id, 'Una red privada', 0, 3
FROM question qu
JOIN quiz q ON q.id = qu.quiz_id
WHERE q.title = 'Demo: Seguridad básica' AND qu.text = '¿Qué es 2FA?'
ORDER BY qu.id DESC
LIMIT 1;
