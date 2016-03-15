package com.apps.gill.showingmaps.models.PlaceAutocomplete;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gill on 22-02-2016.
 */
public class MyLocation {
    public List<Prediction> predictions = new ArrayList<Prediction>();
    public String status;

    public class Prediction {
        public String description;
        public String id;
        public List<MatchedSubstring> matchedSubstrings = new ArrayList<MatchedSubstring>();
        public String placeId;
        public String reference;
        public List<Term> terms = new ArrayList<Term>();
        public List<String> types = new ArrayList<String>();

        public class Term {
            public Integer offset;
            public String value;
        }

        public class MatchedSubstring {
            public Integer length;
            public Integer offset;
        }
    }
}
