
CREATE TABLE beneficiary (
    beneficiary_id uuid NOT NULL PRIMARY KEY,
    account_name character varying(255),
    account_number character varying(255),
    account_number_code character varying(255),
    account_type integer NOT NULL,
    address character varying(255),
    bank_id character varying(255),
    bank_id_code character varying(255),
    name character varying(255)
);

CREATE TABLE debtor (
    debtor_id uuid NOT NULL PRIMARY KEY,
    account_name character varying(255),
    account_number character varying(255),
    account_number_code character varying(255),
    address character varying(255),
    bank_id character varying(255),
    bank_id_code character varying(255),
    name character varying(255)
);

CREATE TABLE sponsor (
    sponsor_id uuid NOT NULL PRIMARY KEY,
    account_number character varying(255),
    bank_id character varying(255),
    bank_id_code character varying(255)
);

CREATE TABLE payment (
    id uuid NOT NULL PRIMARY KEY,
    amount numeric(19,2),
    bearer_code character varying(255),
    receiver_charges_amount numeric(19,2),
    receiver_charges_currency character varying(255),
    currency character varying(255),
    end_to_end_reference character varying(255),
    contract_reference character varying(255),
    exchange_rate numeric(20,5),
    original_amount numeric(19,2),
    original_currency character varying(255),
    numeric_reference character varying(255),
    payment_id character varying(255),
    payment_purpose character varying(255),
    payment_scheme character varying(255),
    payment_type character varying(255),
    processing_date date,
    reference character varying(255),
    scheme_payment_sub_type character varying(255),
    scheme_payment_type character varying(255),
    organisation_id character varying(255),
    type character varying(255),
    version integer NOT NULL,
    beneficiary_party_beneficiary_id uuid REFERENCES beneficiary,
    debtor_party_debtor_id uuid REFERENCES debtor,
    sponsor_party_sponsor_id uuid REFERENCES sponsor
);

CREATE TABLE charge (
    charge_id uuid NOT NULL PRIMARY KEY,
    amount numeric(19,2),
    currency character varying(255)
);

CREATE TABLE payment_sender_charges (
    payment_id uuid NOT NULL REFERENCES payment,
    sender_charges_charge_id uuid NOT NULL UNIQUE references charge
);
