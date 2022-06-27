package com.diegolima.ecommerce.activity.loja;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.diegolima.ecommerce.R;
import com.diegolima.ecommerce.databinding.ActivityLojaFormProdutoBinding;
import com.diegolima.ecommerce.databinding.BottomSheetFormProdutoBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class LojaFormProdutoActivity extends AppCompatActivity {

	private String currentPhotoPath;

	private int resultCode = 0;

	private ActivityLojaFormProdutoBinding binding;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityLojaFormProdutoBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		configClicks();
	}

	private void configClicks() {
		binding.imagemProduto0.setOnClickListener(v -> showBottomSheet(0));
		binding.imagemProduto1.setOnClickListener(v -> showBottomSheet(1));
		binding.imagemProduto2.setOnClickListener(v -> showBottomSheet(2));
	}

	private void showBottomSheet(int code) {
		resultCode = code;

		BottomSheetFormProdutoBinding sheetBinding =
				BottomSheetFormProdutoBinding.inflate(LayoutInflater.from(this));

		BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
				this, R.style.BottomSheetDialog);

		bottomSheetDialog.setContentView(sheetBinding.getRoot());
		bottomSheetDialog.show();

		sheetBinding.btnCamera.setOnClickListener(v -> {
			verificaPermissaoCamera();
			bottomSheetDialog.dismiss();
		});

		sheetBinding.btnGaleria.setOnClickListener(v -> {
			verificaPermissaoGaleria();
			bottomSheetDialog.dismiss();
		});

		sheetBinding.btnCancelar.setOnClickListener(v -> {
			Toast.makeText(this, "Cancelar", Toast.LENGTH_SHORT).show();
			bottomSheetDialog.dismiss();
		});
	}

	private File createImageFile() throws IOException {
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
		File image = File.createTempFile(
				imageFileName,  /* prefix */
				".jpg",         /* suffix */
				storageDir      /* directory */
		);
		currentPhotoPath = image.getAbsolutePath();
		return image;
	}

	private void verificaPermissaoCamera() {
		PermissionListener permissionlistener = new PermissionListener() {
			@Override
			public void onPermissionGranted() {
				abrirCamera();
			}

			@Override
			public void onPermissionDenied(List<String> deniedPermissions) {
				Toast.makeText(getBaseContext(), "Permissão Negada." + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
			}
		};
		showDialogPermissao(
				permissionlistener,
				new String[]{Manifest.permission.CAMERA},
				"Se você não aceitar a permissão não poderá acessar a Câmera do dispositivo, deseja ativar a permissão agora ?"
		);
	}

	private void verificaPermissaoGaleria() {
		PermissionListener permissionlistener = new PermissionListener() {
			@Override
			public void onPermissionGranted() {
				abrirGaleria();
			}

			@Override
			public void onPermissionDenied(List<String> deniedPermissions) {
				Toast.makeText(getBaseContext(), "Permissão Negada." + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
			}
		};
		showDialogPermissao(
				permissionlistener,
				new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
				"Se você não aceitar a permissão não poderá acessar a Galeria do dispositivo, deseja ativar a permissão agora ?"
		);
	}

	private void abrirCamera() {
		switch (resultCode) {
			case 0:
				resultCode = 3;
				break;
			case 1:
				resultCode = 4;
				break;
			case 2:
				resultCode = 5;
				break;
		}

		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// Create the File where the photo should go
		File photoFile = null;
		try {
			photoFile = createImageFile();
		} catch (IOException ex) {
			// Error occurred while creating the File
		}
		// Continue only if the File was successfully created
		if (photoFile != null) {
			Uri photoURI = FileProvider.getUriForFile(this,
					"com.diegolima.ecommerce.fileprovider",
					photoFile);
			takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
			resultLauncher.launch(takePictureIntent);
		}
	}

	private void abrirGaleria() {
		Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		resultLauncher.launch(intent);
	}

	private final ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
			new ActivityResultContracts.StartActivityForResult(),
			result -> {
				if (result.getResultCode() == RESULT_OK) {
					Bitmap bitmap0;
					Bitmap bitmap1;
					Bitmap bitmap2;
					String caminhoImagem;
					if (resultCode <= 2) {
						Uri imagemSelecionada = result.getData().getData();
						try {
							caminhoImagem = imagemSelecionada.toString();
							switch (resultCode) {
								case 0: {
									binding.imageFake0.setVisibility(View.GONE);
									binding.imagemProduto0.setImageBitmap(getBitmap(imagemSelecionada));
									break;
								}
								case 1: {
									binding.imageFake1.setVisibility(View.GONE);
									binding.imagemProduto1.setImageBitmap(getBitmap(imagemSelecionada));
									break;
								}
								case 2: {
									binding.imageFake2.setVisibility(View.GONE);
									binding.imagemProduto2.setImageBitmap(getBitmap(imagemSelecionada));
									break;
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else { //camera
						File file = new File(currentPhotoPath);
						caminhoImagem = String.valueOf(file.toURI());
						switch (resultCode) {
							case 3:
								binding.imageFake0.setVisibility(View.GONE);
								binding.imagemProduto0.setImageURI(Uri.fromFile(file));
								break;
							case 4:
								binding.imageFake1.setVisibility(View.GONE);
								binding.imagemProduto1.setImageURI(Uri.fromFile(file));
								break;
							case 5:
								binding.imageFake2.setVisibility(View.GONE);
								binding.imagemProduto2.setImageURI(Uri.fromFile(file));
								break;
						}
					}
				}
			}
	);

	private Bitmap getBitmap(Uri caminhoUri) {

		Bitmap bitmap = null;

		try {
			if (Build.VERSION.SDK_INT < 28) {
				bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), caminhoUri);
			} else {
				ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), caminhoUri);
				bitmap = ImageDecoder.decodeBitmap(source);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bitmap;
	}

	private void showDialogPermissao(PermissionListener permissionListener, String[] permissoes, String msg) {
		TedPermission.create()
				.setPermissionListener(permissionListener)
				.setDeniedTitle("Permissões")
				.setDeniedMessage(msg)
				.setDeniedCloseButtonText("Não")
				.setGotoSettingButtonText("Sim")
				.setPermissions(permissoes)
				.check();
	}
}