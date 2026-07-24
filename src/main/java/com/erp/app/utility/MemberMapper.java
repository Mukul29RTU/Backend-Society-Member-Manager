package com.erp.app.utility;

import java.util.Map;
import com.erp.app.entities.BaseMember;

public class MemberMapper {

    /**
     * Maps a map of data (supporting both Hindi and underscore keys) to a Member
     * entity.
     * 
     * @param data   The source map
     * @param member The target member entity
     * @param <T>    The type of Member
     * @return The updated member entity
     */
    public static <T extends BaseMember> T mapToEntity(Map<String, Object> data, T member) {
        if (data == null || member == null)
            return member;

        data.forEach((key, value) -> {
            if (value == null)
                return;

            String stringValue = value.toString();

            switch (key) {
                case "सदस्य नंबर":
                case "सदस्य_नंबर":
                    if (value instanceof Number) {
                        member.setसदस्य_नंबर(((Number) value).intValue());
                    } else {
                        try {
                            member.setसदस्य_नंबर(Integer.parseInt(stringValue));
                        } catch (NumberFormatException ignored) {
                        }
                    }
                    break;
                case "पूर्ण जानकारी":
                case "पूर्ण_जानकारी":
                    member.setपूर्ण_जानकारी(stringValue);
                    break;
                case "क्रमांक":
                    member.setक्रमांक(stringValue);
                    break;
                case "क्रमांक संख्या":
                case "क्रमांक_संख्या":
                    member.setक्रमांक_संख्या(stringValue);
                    break;
                case "नाम":
                    member.setनाम(stringValue);
                    break;
                case "पहचान":
                    member.setपहचान(stringValue);
                    break;
                case "पता":
                    member.setपता(stringValue);
                    break;
                case "संपर्क":
                    if (value instanceof Number) {
                        member.setसंपर्क(((Number) value).longValue());
                    } else {
                        try {
                            member.setसंपर्क(Long.parseLong(stringValue));
                        } catch (NumberFormatException ignored) {
                        }
                    }
                    break;
                case "वार्ड संख्या":
                case "वार्ड_संख्या":
                    member.setवार्ड_संख्या(stringValue);
                    break;
                case "email":
                    member.setEmail(stringValue);
                    break;
            }
        });

        return member;
    }
}
