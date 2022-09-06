using UnityEngine;
using TMPro;
using UnityEngine.VFX;

public class NEWWeaponScript : MonoBehaviour
{

    public Camera fpsCam;
    public float damage = 10f;
    public float range = 100f;

    public float hitForce = 10f;
    public VisualEffect muzzleFlash;
    public GameObject impactEffect;
    public GameObject bloodEffect;

    public Transform flashPoint;

    public TextMeshProUGUI ammunitionDisplay;

    public float interval, spread, reloadT, shotInterval;
    public bool allowButtonHold;
    public bool allowInvoke = true;
    public int ammo, bulletsPerTap;
    int ammoLeft, ammoShot;
    bool shooting, readyToShoot, reloading;
    private void Awake(){
        ammoLeft = ammo;
        readyToShoot = true;
    }
    void Update()
    {
        MyInput();
        if(ammunitionDisplay != null){
            ammunitionDisplay.SetText(ammoLeft/bulletsPerTap + " / "+ammo/bulletsPerTap);
        }
        else{
            ammunitionDisplay.SetText("");
        }
    }

    private void MyInput()
    {
        //Check if allowed to hold down button
        if (allowButtonHold) shooting = Input.GetKey(KeyCode.Mouse0);
        else shooting = Input.GetKeyDown(KeyCode.Mouse0);

        //REloading
        if (Input.GetKeyDown(KeyCode.R) && ammoLeft < ammo){
            Reload();

        }

        if(readyToShoot && shooting && !reloading && ammoLeft<=0) Reload();

        //shooting
        if (readyToShoot && shooting && !reloading && ammoLeft >0)
        {
                ammoShot = 0;

                Shoot();
        }

        
    }
    void Shoot()
    {
        readyToShoot = false;
        if(muzzleFlash != null){
            muzzleFlash.Play();
        }
        
        RaycastHit hit;
        if(Physics.Raycast(fpsCam.transform.position, fpsCam.transform.forward, out hit, range))
        {
            Debug.Log(hit.transform.name + "hit");
            EnemyScript enemy = hit.transform.GetComponent<EnemyScript>();
            if(enemy != null){
                enemy.TakeDamage(damage);
                if(bloodEffect != null){
                    GameObject impactGO = Instantiate(bloodEffect, hit.point, Quaternion.LookRotation(hit.normal));
                    Destroy(impactGO, 2f);
                }
            }
            else{
                if(impactEffect != null){
                    GameObject impactGO = Instantiate(impactEffect, hit.point,Quaternion.LookRotation(hit.normal));
                    Destroy(impactGO, 1f);
                }
            }

            if(hit.rigidbody != null){
                //Impact force
                hit.rigidbody.AddForce(-hit.normal*hitForce);
                //10f voi korvata muuttujalla jolloin voiman määrä skaalaa halutusti
            }
            
        }

        ammoLeft--;
        ammoShot++;

        if (allowInvoke){
            Invoke("ResetShot", interval);
            allowInvoke = false;

            //Recoil to player
            //playerRb.AddForce(-directionWithSpread.normalized * recoilForce, ForceMode.Impulse);
        }

        if(ammoShot < bulletsPerTap && ammoLeft >0){
            Invoke("Shoot", shotInterval);
        }

        
        
    }

    private void ResetShot(){
        readyToShoot = true;
        allowInvoke = true;
    }

    private void Reload(){
        reloading = true;
        Invoke("ReloadFinished", reloadT);
    }

    private void ReloadFinished(){
        ammoLeft = ammo;
        reloading = false;
    }
}
