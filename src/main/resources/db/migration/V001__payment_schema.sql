
CREATE TABLE beneficiary (
    beneficiary_id uuid NOT NULL PRIMARY KEY,
    account_name character varying(255) NOT NULL,
    account_number character varying(255) NOT NULL,
    account_number_code character varying(255) NOT NULL,
    account_type integer NOT NULL,
    address character varying(255) NOT NULL,
    bank_id character varying(255) NOT NULL,
    bank_id_code character varying(255) NOT NULL,
    name character varying(255) NOT NULL
);

CREATE TABLE debtor (
    debtor_id uuid NOT NULL PRIMARY KEY,
    account_name character varying(255) NOT NULL,
    account_number character varying(255) NOT NULL,
    account_number_code character varying(255) NOT NULL,
    address character varying(255) NOT NULL,
    bank_id character varying(255) NOT NULL,
    bank_id_code character varying(255) NOT NULL,
    name character varying(255) NOT NULL
);

CREATE TABLE sponsor (
    sponsor_id uuid NOT NULL PRIMARY KEY NOT NULL,
    account_number character varying(255) NOT NULL,
    bank_id character varying(255) NOT NULL,
    bank_id_code character varying(255) NOT NULL
);

CREATE TABLE payment (
    id uuid NOT NULL PRIMARY KEY,
    amount numeric(19,2) NOT NULL,
    bearer_code character varying(255) NOT NULL,
    receiver_charges_amount numeric(19,2) NOT NULL,
    receiver_charges_currency character varying(255) NOT NULL,
    currency character varying(255) NOT NULL,
    end_to_end_reference character varying(255) NOT NULL,
    contract_reference character varying(255),
    exchange_rate numeric(20,5),
    original_amount numeric(19,2),
    original_currency character varying(255),
    numeric_reference character varying(255) NOT NULL,
    payment_id character varying(255) NOT NULL UNIQUE,
    payment_purpose character varying(255) NOT NULL,
    payment_scheme character varying(255) NOT NULL,
    payment_type character varying(255) NOT NULL,
    processing_date date NOT NULL,
    reference character varying(255) NOT NULL,
    scheme_payment_sub_type character varying(255) NOT NULL,
    scheme_payment_type character varying(255) NOT NULL,
    organisation_id character varying(255) NOT NULL,
    type character varying(255) NOT NULL,
    version integer NOT NULL,
    beneficiary_party_beneficiary_id uuid NOT NULL REFERENCES beneficiary,
    debtor_party_debtor_id uuid NOT NULL REFERENCES debtor,
    sponsor_party_sponsor_id uuid NOT NULL REFERENCES sponsor
);

CREATE TABLE charge (
    charge_id uuid NOT NULL PRIMARY KEY,
    amount numeric(19,2) NOT NULL,
    currency character varying(255) NOT NULL
);

CREATE TABLE payment_sender_charges (
    payment_id uuid NOT NULL REFERENCES payment,
    sender_charges_charge_id uuid NOT NULL UNIQUE references charge
);
