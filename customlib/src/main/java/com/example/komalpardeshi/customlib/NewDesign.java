package com.example.komalpardeshi.customlib;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewDesign.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewDesign#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewDesign extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private SimpleExoPlayer mSimpleExoplayer;
    private PlayerView mPlayerView;
    private OnFragmentInteractionListener mListener;

    public NewDesign() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewDesign.
     */
    // TODO: Rename and change types and number of parameters
    public static NewDesign newInstance(String param1, String param2) {
        NewDesign fragment = new NewDesign();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_design, container, false);

        mPlayerView = view.findViewById(R.id.exoplayer);
        makeStream(mPlayerView);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public SimpleExoPlayer makeStream(PlayerView playerView) {

        this.mPlayerView = playerView;
        mSimpleExoplayer = ExoPlayerFactory.newSimpleInstance(getActivity(), new DefaultTrackSelector());
        mPlayerView.setPlayer(mSimpleExoplayer);


        DefaultDataSourceFactory defaultExtractorsFactory =
                new DefaultDataSourceFactory(getActivity(), Util.getUserAgent(getActivity(), "exo-demo"));
//        Uri uri = Uri.parse("https://bitmovin-a.akamaihd.net/content/playhouse-vr/mpds/105560.mpd");
//        Uri uriMp4 =
//                Uri.parse(
//                        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4");
//        Uri uriHls =
//                Uri.parse(
//                        "https://devstreaming-cdn.apple.com/videos/streaming/examples/bipbop_4x3/bipbop_4x3_variant.m3u8");
//        Uri uriSs =
//                Uri.parse(
//                        "https://playready.directtaps.net/smoothstreaming/SSWSS720H264/SuperSpeedway_720.ism");

        String filename = "https://bitmovin-a.akamaihd.net/content/playhouse-vr/mpds/105560.mpd";
        String filenameArray[] = filename.split("\\.");
        String extension = filenameArray[filenameArray.length - 1];
        Uri videoUri = Uri.parse(filename);

        DashMediaSource dashMediaSource;
        HlsMediaSource hlsMediaSource;
        ExtractorMediaSource extractorMediaSource;
        SsMediaSource ssMediaSource = null;

        if (extension.equals("mp4")) {
            extractorMediaSource = new ExtractorMediaSource.Factory(defaultExtractorsFactory)
                    .createMediaSource(videoUri);
            mSimpleExoplayer.prepare(extractorMediaSource);
        } else if (extension.equals("mpd")) {
            dashMediaSource = new
                    DashMediaSource.Factory(defaultExtractorsFactory).createMediaSource(videoUri);
            mSimpleExoplayer.prepare(dashMediaSource);

        } else if (extension.equals("m3u8")) {
            hlsMediaSource = new
                    HlsMediaSource.Factory(defaultExtractorsFactory).createMediaSource(videoUri);
            mSimpleExoplayer.prepare(hlsMediaSource);
        } else if (extension.equals("ism")) {
            ssMediaSource = new SsMediaSource.Factory(defaultExtractorsFactory).createMediaSource(videoUri);
            mSimpleExoplayer.prepare(ssMediaSource);

        }

        mSimpleExoplayer.setPlayWhenReady(true);

        return mSimpleExoplayer;
    }

}
