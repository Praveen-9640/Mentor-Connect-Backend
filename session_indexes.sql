-- MySQL session indexes for MentorConnect
CREATE INDEX idx_session_mentor ON session(mentor_id);
CREATE INDEX idx_session_mentee ON session(mentee_id);
CREATE INDEX idx_session_start ON session(start_time);
CREATE INDEX idx_session_end ON session(end_time);
