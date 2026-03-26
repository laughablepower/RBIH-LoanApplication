CREATE TABLE IF NOT EXISTS loan_applications (

                                   id UUID PRIMARY KEY,

                                   applicant_name VARCHAR(255),
                                   applicant_age INT,
                                   monthly_income DOUBLE,

                                   employment_type VARCHAR(50),

                                   credit_score INT,
                                   loan_amount DECIMAL(19, 2),
                                   tenure_months INT,

                                   purpose VARCHAR(50),
                                   status VARCHAR(50),
                                   risk_band VARCHAR(50),

                                   interest_rate DECIMAL(5, 2),
                                   emi DECIMAL(19, 2),
                                   total_payable DECIMAL(19, 2),

                                   rejection_reasons VARCHAR(500),

                                   created_at TIMESTAMP
);