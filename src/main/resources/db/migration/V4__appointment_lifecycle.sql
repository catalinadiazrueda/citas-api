CREATE TABLE IF NOT EXISTS reschedule_statuses (
    id SMALLINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(30) NOT NULL UNIQUE,
    name VARCHAR(80) NOT NULL,
    is_terminal BOOLEAN NOT NULL DEFAULT FALSE
) ENGINE=InnoDB;

INSERT INTO reschedule_statuses(id, code, name, is_terminal) VALUES
    (1, 'PENDING', 'Pendiente', FALSE),
    (2, 'APPROVED', 'Aprobada', TRUE),
    (3, 'REJECTED', 'Rechazada', TRUE)
ON DUPLICATE KEY UPDATE name=VALUES(name), is_terminal=VALUES(is_terminal);

CREATE TABLE IF NOT EXISTS reschedule_requests (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    appointment_id BIGINT UNSIGNED NOT NULL,
    status_id SMALLINT UNSIGNED NOT NULL,
    requested_start_at DATETIME NOT NULL,
    requested_end_at DATETIME NOT NULL,
    requested_by_user_id BIGINT UNSIGNED NOT NULL,
    reviewed_by_user_id BIGINT UNSIGNED NULL,
    reviewed_at DATETIME NULL,
    decision_reason VARCHAR(500) NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_reschedule_appointment FOREIGN KEY (appointment_id) REFERENCES appointments(id),
    CONSTRAINT fk_reschedule_status FOREIGN KEY (status_id) REFERENCES reschedule_statuses(id),
    CONSTRAINT fk_reschedule_requester FOREIGN KEY (requested_by_user_id) REFERENCES users(id),
    CONSTRAINT fk_reschedule_reviewer FOREIGN KEY (reviewed_by_user_id) REFERENCES users(id),
    INDEX ix_reschedule_status (status_id, requested_start_at),
    INDEX ix_reschedule_appointment (appointment_id)
) ENGINE=InnoDB;

ALTER TABLE professional_slots ADD COLUMN reschedule_request_id BIGINT UNSIGNED NULL;
ALTER TABLE professional_slots ADD CONSTRAINT fk_slot_reschedule_request FOREIGN KEY (reschedule_request_id) REFERENCES reschedule_requests(id) ON DELETE SET NULL;
CREATE INDEX ix_slots_reschedule_request ON professional_slots(reschedule_request_id);
