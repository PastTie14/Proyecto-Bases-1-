CREATE OR REPLACE PACKAGE BODY adminConsult AS

    FUNCTION getDonations(
        pStartDate     IN DATE,
        pEndDate       IN DATE,
        pIdDonor       IN NUMBER,
        pIdAssociation IN NUMBER
    ) RETURN SYS_REFCURSOR IS
        vCursor SYS_REFCURSOR;
    BEGIN
        OPEN vCursor FOR
            SELECT
                d.id_donnor,
                u_don.email                        AS donor_email,
                u_aso.email                        AS association_email,
                MIN(d.createdAt)                   AS earliest_donation,
                COUNT(d.id_donation)               AS total_donations,
                SUM(d.amount)                      AS total_amount,
                cu.acronym                         AS currency,
                COUNT(*) OVER ()                   AS total_records,
                SUM(SUM(d.amount)) OVER ()         AS grand_total_amount
            FROM      donation    d
            JOIN      "user"      u_don  ON u_don.id_user  = d.id_donnor
            JOIN      association a      ON a.id_user      = d.id_association
            JOIN      "user"      u_aso  ON u_aso.id_user  = a.id_user
            JOIN      currency    cu     ON cu.id_currency = d.id_currency
            WHERE (pStartDate     IS NULL OR TRUNC(d.createdAt)  >= TRUNC(pStartDate))
              AND (pEndDate       IS NULL OR TRUNC(d.createdAt)  <= TRUNC(pEndDate))
              AND (pIdDonor       IS NULL OR d.id_donnor         =  pIdDonor)
              AND (pIdAssociation IS NULL OR d.id_association    =  pIdAssociation)
            GROUP BY
                d.id_donnor,
                u_don.email,
                u_aso.email,
                cu.acronym
            ORDER BY total_amount DESC;
        RETURN vCursor;
    END getDonations;
    
    
    
    FUNCTION getBlackListReport RETURN SYS_REFCURSOR IS
        vCursor SYS_REFCURSOR;
    BEGIN
        OPEN vCursor FOR
            SELECT
                uxbl.id_user,
                u.email                                              AS user_email,
                NVL(
                    ad.first_name  || ' '
                        || NVL(ad.second_name  || ' ', '')
                        || ad.first_surname || ' '
                        || NVL(ad.second_surname, ''),
                    u.email
                )                                                    AS full_name,
                COUNT(DISTINCT uxbl.id_report)                       AS total_reports,
                ROUND(AVG(rt.score), 2)                              AS avg_rating,
                MAX(bl.createdAt)                                    AS latest_report_date,
                COUNT(*) OVER ()                                     AS total_records
            FROM       user_x_black_list  uxbl
            JOIN       black_list         bl   ON bl.id_report   = uxbl.id_report
            JOIN       "user"             u    ON u.id_user      = uxbl.id_user
            LEFT JOIN  adopter            ad   ON ad.id_user     = uxbl.id_user
            LEFT JOIN  rating             rt   ON rt.id_adopter  = uxbl.id_user
            GROUP BY
                uxbl.id_user,
                u.email,
                ad.first_name,
                ad.second_name,
                ad.first_surname,
                ad.second_surname
            ORDER BY total_reports DESC, latest_report_date DESC;
        RETURN vCursor;
    END getBlackListReport;

    FUNCTION getBlackListReportDetails(pIdUser IN NUMBER) RETURN SYS_REFCURSOR IS
        vCursor SYS_REFCURSOR;
    BEGIN
        OPEN vCursor FOR
            SELECT
                bl.id_report,
                uxbl.reason,
                bl.createdAt                                          AS reported_date,
                u_rep.email                                           AS reporter_email,
                NVL(
                    NVL(
                        ad_rep.first_name  || ' ' || ad_rep.first_surname,
                        rs_rep.first_name  || ' ' || rs_rep.first_surname
                    ),
                    u_rep.email
                )                                                     AS reporter_name,
                rt.score                                              AS user_rating,
                COUNT(*) OVER ()                                      AS total_reports
            FROM       user_x_black_list  uxbl
            JOIN       black_list         bl      ON bl.id_report     = uxbl.id_report
            JOIN       "user"             u_rep   ON u_rep.id_user    = bl.id_user
            LEFT JOIN  adopter            ad_rep  ON ad_rep.id_user   = u_rep.id_user
            LEFT JOIN  rescuer            rs_rep  ON rs_rep.id_user   = u_rep.id_user
            LEFT JOIN  rating             rt      ON rt.id_adopter    = uxbl.id_user
            WHERE uxbl.id_user = pIdUser
            ORDER BY bl.createdAt DESC;
        RETURN vCursor;
    END getBlackListReportDetails;
 
    FUNCTION getMatches(
        pIdType IN NUMBER,
        pIdRace IN NUMBER
    ) RETURN SYS_REFCURSOR IS
        vCursor SYS_REFCURSOR;
    BEGIN
        OPEN vCursor FOR
            SELECT
                m.id_match,
                m.match_date,
                m.similarity_percentage,
                p.id_parameter,
                vt."type"                        AS parameter_type,
                p."value"                        AS parameter_value,
                m.createdAt                      AS created_date,
                COUNT(*) OVER ()                 AS total_records
            FROM       match       m
            JOIN       parameters  p   ON p.id_match      = m.id_match
            JOIN       value_type  vt  ON vt.id_value_type = p.id_value_type
            WHERE  (pIdType IS NULL OR EXISTS (
                        SELECT 1
                        FROM  parameters  pi
                        JOIN  value_type  vti ON vti.id_value_type = pi.id_value_type
                        WHERE pi.id_match          = m.id_match
                          AND UPPER(vti."type")    = 'PET_TYPE'
                          AND pi."value"           = TO_CHAR(pIdType)
                   ))
              AND  (pIdRace IS NULL OR EXISTS (
                        SELECT 1
                        FROM  parameters  pr
                        JOIN  value_type  vtr ON vtr.id_value_type = pr.id_value_type
                        WHERE pr.id_match          = m.id_match
                          AND UPPER(vtr."type")    = 'RACE'
                          AND pr."value"           = TO_CHAR(pIdRace)
                   ))
            ORDER BY m.similarity_percentage DESC, m.match_date DESC;
        RETURN vCursor;
    END getMatches;
 
    FUNCTION getPetNecessaryTreatments(
        pMin IN NUMBER,
        pMax IN NUMBER
    ) RETURN SYS_REFCURSOR IS
        vCursor SYS_REFCURSOR;
    BEGIN
        OPEN vCursor FOR
            SELECT
                p.id_pet,
                p.first_name                              AS pet_name,
                pt."name"                                 AS pet_type,
                st.status_type                            AS pet_status,
                COUNT(DISTINCT dxms.id_disease)           AS disease_count,
                COUNT(DISTINCT txd.id_treatment)          AS treatment_count,
                COUNT(*) OVER ()                          AS total_records
            FROM       pet                    p
            JOIN       pet_type               pt    ON pt.id_pet_type       = p.id_pet_type
            JOIN       status                 st    ON st.id_status         = p.id_status
            JOIN       pet_extra_info         pei   ON pei.id_pet           = p.id_pet
            JOIN       medic_sheet            ms    ON ms.id_pet_extra_info = pei.id_pet_extra_info
            JOIN       disease_x_medic_sheet  dxms  ON dxms.id_medic_sheet  = ms.id_medic_sheet
            LEFT JOIN  treatment_x_disease    txd   ON txd.id_disease       = dxms.id_disease
            GROUP BY
                p.id_pet,
                p.first_name,
                pt."name",
                st.status_type
            HAVING
                (pMin IS NULL OR COUNT(DISTINCT dxms.id_disease) >= pMin)
            AND (pMax IS NULL OR COUNT(DISTINCT dxms.id_disease) <= pMax)
            ORDER BY disease_count DESC;
        RETURN vCursor;
    END getPetNecessaryTreatments;
 
    FUNCTION getCompatibleCribHouses(pIdPetType IN NUMBER) RETURN SYS_REFCURSOR IS
        vCursor SYS_REFCURSOR;
    BEGIN
        OPEN vCursor FOR
            SELECT
                ch.id_user                                              AS id_crib_house,
                ch."name"                                               AS crib_house_name,
                u.email,
                ch.requires_donations,
                sz."name"                                               AS accepted_size,
                COUNT(sz.id_size) OVER (PARTITION BY ch.id_user)       AS sizes_accepted_count,
                COUNT(*) OVER ()                                        AS total_records
            FROM       crib_house             ch
            JOIN       "user"                 u      ON u.id_user        = ch.id_user
            JOIN       pet_type_x_crib_house  ptxch  ON ptxch.id_crib_house = ch.id_user
            LEFT JOIN  size_x_crib_house      sxch   ON sxch.id_crib_house  = ch.id_user
            LEFT JOIN  "size"                 sz     ON sz.id_size          = sxch.id_size
            WHERE (pIdPetType IS NULL OR ptxch.id_pet_type = pIdPetType)
            ORDER BY ch."name", sz."name";
        RETURN vCursor;
    END getCompatibleCribHouses;
 
    FUNCTION getBestRescuersAndAdopters(
        pStartDate IN DATE,
        pEndDate   IN DATE
    ) RETURN SYS_REFCURSOR IS
        vCursor SYS_REFCURSOR;
    BEGIN
        OPEN vCursor FOR
            SELECT
                user_type,
                id_user,
                email,
                full_name,
                activity_count,
                COUNT(*) OVER ()   AS total_records
            FROM (
                SELECT
                    'RESCUER'                                               AS user_type,
                    rs.id_user,
                    u.email,
                    rs.first_name  || ' '
                        || NVL(rs.second_name  || ' ', '')
                        || rs.first_surname    || ' '
                        || NVL(rs.second_surname, '')                       AS full_name,
                    COUNT(p.id_pet)                                         AS activity_count
                FROM       rescuer  rs
                JOIN       "user"   u  ON u.id_user    = rs.id_user
                JOIN       pet      p  ON p.id_rescuer = rs.id_user
                WHERE (pStartDate IS NULL OR TRUNC(p.createdAt) >= TRUNC(pStartDate))
                  AND (pEndDate   IS NULL OR TRUNC(p.createdAt) <= TRUNC(pEndDate))
                GROUP BY
                    rs.id_user, u.email,
                    rs.first_name, rs.second_name,
                    rs.first_surname, rs.second_surname
 
                UNION ALL
                 SELECT
                    'ADOPTER'                                               AS user_type,
                    ad.id_user,
                    u.email,
                    ad.first_name  || ' '
                        || NVL(ad.second_name  || ' ', '')
                        || ad.first_surname    || ' '
                        || NVL(ad.second_surname, '')                       AS full_name,
                    COUNT(af.id_adoption)                                   AS activity_count
                FROM       adopter        ad
                JOIN       "user"         u   ON u.id_user     = ad.id_user
                JOIN       adoption_form  af  ON af.id_adopter = ad.id_user
                WHERE (pStartDate IS NULL OR TRUNC(af.adoption_date) >= TRUNC(pStartDate))
                  AND (pEndDate   IS NULL OR TRUNC(af.adoption_date) <= TRUNC(pEndDate))
                GROUP BY
                    ad.id_user, u.email,
                    ad.first_name, ad.second_name,
                    ad.first_surname, ad.second_surname
            )
            ORDER BY activity_count DESC, user_type;
        RETURN vCursor;
    END getBestRescuersAndAdopters;
 
END adminConsult;