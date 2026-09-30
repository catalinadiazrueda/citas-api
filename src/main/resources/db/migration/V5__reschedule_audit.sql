CREATE TABLE IF NOT EXISTS reschedule_status_history (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    reschedule_request_id BIGINT UNSIGNED NOT NULL,
    status_id SMALLINT UNSIGNED NOT NULL,
    changed_by_user_id BIGINT UNSIGNED NULL,
    change_source VARCHAR(20) NOT NULL,
    reason VARCHAR(500) NULL,
    changed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_reschedule_history_request FOREIGN KEY (reschedule_request_id) REFERENCES reschedule_requests(id) ON DELETE CASCADE,
    CONSTRAINT fk_reschedule_history_status FOREIGN KEY (status_id) REFERENCES reschedule_statuses(id),
    CONSTRAINT fk_reschedule_history_actor FOREIGN KEY (changed_by_user_id) REFERENCES users(id),
    INDEX ix_reschedule_history_request (reschedule_request_id, changed_at)
) ENGINE=InnoDB;
