-- CREACION DE TABLAS PARA LOS CUESTIONARIOS Y RESPUESTAS

-- USE localhost;

-- 1) CUESTIONARIO
CREATE TABLE `quiz` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `title` TEXT NOT NULL,
  `description` TEXT,
  `is_published` TINYINT(1) NOT NULL DEFAULT 0,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- 2) PREGUNTA
CREATE TABLE `question` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `quiz_id` BIGINT NOT NULL,
  `text` TEXT NOT NULL,
  `ord` INT NOT NULL,
  CONSTRAINT `fk_question_quiz`
    FOREIGN KEY (`quiz_id`) REFERENCES `quiz`(`id`)
    ON DELETE CASCADE,
  KEY `ix_question_quiz` (`quiz_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- 3) OPCIÓN (usa 'option_item' porque OPTION es reservada en MySQL)
--    EXACTAMENTE UNA opción correcta por pregunta (se valida en backend)
CREATE TABLE `option_item` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `question_id` BIGINT NOT NULL,
  `text` TEXT NOT NULL,
  `is_correct` TINYINT(1) NOT NULL DEFAULT 0,
  `ord` INT NOT NULL,
  CONSTRAINT `fk_option_question`
    FOREIGN KEY (`question_id`) REFERENCES `question`(`id`)
    ON DELETE CASCADE,
  KEY `ix_option_question` (`question_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- 4) ENVÍO/RESULTADO
CREATE TABLE `submission` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `quiz_id` BIGINT NOT NULL,       -- a qué quiz responde
  `user_id` BIGINT NOT NULL,       -- quién respondió (FK opcional si tienes tabla usuarios)
  `score` INT NOT NULL,            -- 0..100, guardado para listados rápidos
  `finished_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT `fk_submission_quiz`
    FOREIGN KEY (`quiz_id`) REFERENCES `quiz`(`id`),
  KEY `ix_submission_quiz` (`quiz_id`),
  KEY `ix_submission_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- 5) RESPUESTAS DE ESA ENTREGA (una fila por pregunta)
CREATE TABLE `submission_answer` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `submission_id` BIGINT NOT NULL,
  `question_id` BIGINT NOT NULL,
  `selected_option_id` BIGINT NOT NULL,
  `is_correct` TINYINT(1) NOT NULL,
  CONSTRAINT `fk_sa_submission`
    FOREIGN KEY (`submission_id`) REFERENCES `submission`(`id`)
    ON DELETE CASCADE,
  CONSTRAINT `fk_sa_question`
    FOREIGN KEY (`question_id`) REFERENCES `question`(`id`),
  CONSTRAINT `fk_sa_option`
    FOREIGN KEY (`selected_option_id`) REFERENCES `option_item`(`id`),
  KEY `ix_sa_submission` (`submission_id`),
  KEY `ix_sa_question` (`question_id`),
  KEY `ix_sa_option` (`selected_option_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;